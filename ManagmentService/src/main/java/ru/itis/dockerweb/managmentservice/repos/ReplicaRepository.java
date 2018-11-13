package ru.itis.dockerweb.managmentservice.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itis.dockerweb.managmentservice.models.Replica;
import ru.itis.dockerweb.managmentservice.models.Status;

import java.util.List;

/**
 * @author Bulat Giniyatullin
 * 11 November 2018
 */

@Repository
public interface ReplicaRepository extends CrudRepository<Replica, String> {
}
