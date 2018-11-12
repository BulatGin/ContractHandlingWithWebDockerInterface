package ru.itis.dockerweb.managmentservice.services.implement;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.dockerweb.managmentservice.models.Replica;
import ru.itis.dockerweb.managmentservice.models.Status;
import ru.itis.dockerweb.managmentservice.repos.ReplicaRepository;
import ru.itis.dockerweb.managmentservice.services.interfaces.ReplicaService;

import java.util.List;

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
        Replica replica = replicaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Illegal id"));
        replica.setStatus(Status.STOPPED);
        replicaRepository.save(replica);
    }

    @Override
    public List<Replica> getAll() {
        return (List<Replica>) replicaRepository.findAll();
    }
}
