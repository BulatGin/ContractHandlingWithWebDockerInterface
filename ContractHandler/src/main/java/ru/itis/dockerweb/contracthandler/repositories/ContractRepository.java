package ru.itis.dockerweb.contracthandler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.dockerweb.contracthandler.models.Contract;

/**
 * @author Bulat Giniyatullin
 * 10 November 2018
 */

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
}
