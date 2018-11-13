package ru.itis.dockerweb.managmentservice.services.interfaces;

import com.spotify.docker.client.DockerException;
import ru.itis.dockerweb.managmentservice.models.Replica;

import java.util.List;

/**
 * @author Bulat Giniyatullin
 * 11 November 2018
 */

public interface ReplicaService {
    String createReplica() throws DockerException, InterruptedException;
    void removeReplica(String id) throws DockerException, InterruptedException;

    List<Replica> getAll();

    List<Replica> pullReplicasUpdate() throws InterruptedException;
    //List<?> getStatsList();
}
