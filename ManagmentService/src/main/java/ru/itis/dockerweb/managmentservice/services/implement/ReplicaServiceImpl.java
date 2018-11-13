package ru.itis.dockerweb.managmentservice.services.implement;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.dockerweb.managmentservice.models.Replica;
import ru.itis.dockerweb.managmentservice.models.Status;
import ru.itis.dockerweb.managmentservice.repos.ReplicaRepository;
import ru.itis.dockerweb.managmentservice.services.interfaces.ReplicaService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Bulat Giniyatullin
 * 11 November 2018
 */

@Service
public class ReplicaServiceImpl implements ReplicaService {
    @Autowired
    private ReplicaRepository replicaRepository;
    @Autowired
    private DockerClient dockerClient;
    @Value("${replicableService.image.name}")
    private String imageName;
    @Value("${replicableService.memory.limit}")
    private Long memoryLimit;
    @Value("${replicableService.network.id}")
    private String networkId;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public String createReplica() throws DockerException, InterruptedException {
        ContainerCreation containerCreation = dockerClient.createContainer(
                ContainerConfig.builder()
                        .image(imageName)
                        .memory(memoryLimit)
                        .build());
        dockerClient.connectToNetwork(containerCreation.id(), networkId);
        dockerClient.startContainer(containerCreation.id());

        replicaRepository.save(Replica.builder()
                .containerId(containerCreation.id())
                .status(Status.RUNNING)
                .build());

        return containerCreation.id();
    }

    @Override
    public void removeReplica(String id) throws DockerException, InterruptedException, IllegalArgumentException {
        dockerClient.stopContainer(id, 10);
        // TODO deleting container after timeout
        Replica replica = replicaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Illegal id"));
        replica.setStatus(Status.STOPPED);
        replicaRepository.save(replica);
    }

    @Override
    public List<Replica> getAll() {
        return (List<Replica>) replicaRepository.findAll();
    }

    @Override
    public List<Replica> pullReplicasUpdate() throws InterruptedException {
        List<Replica> activeReplicas = this.getAll();

        Collection<Callable<Void>> tasks = new HashSet<>();

        for (Replica replica : activeReplicas) {
            if (Status.RUNNING.equals(replica.getStatus())) {
                tasks.add(() -> {
                    ContainerStats stats = dockerClient.stats(replica.getContainerId());
                    replica.setMemoryUsage(stats.memoryStats().usage());
                    replica.setCpu(calculateCPUPercent(stats.precpuStats(), stats.cpuStats()));
                    // TODO if container not found excep., initiate deleting container from redis
                    // and set DELETED status
                    return null;
                });
            }
        }

        executorService.invokeAll(tasks);
        return activeReplicas;
    }

    private static Double calculateCPUPercent(CpuStats preCpuStats, CpuStats cpuStats) {
        double cpuPercent = 0.0;
        long cpuDelta = cpuStats.cpuUsage().totalUsage() - preCpuStats.cpuUsage().totalUsage();
        long systemDelta = cpuStats.systemCpuUsage() - preCpuStats.systemCpuUsage();
        if (systemDelta > 0 && cpuDelta > 0) {
            cpuPercent = ((double)cpuDelta / (double)systemDelta) * 100;
        }
        return cpuPercent;
    }
}
