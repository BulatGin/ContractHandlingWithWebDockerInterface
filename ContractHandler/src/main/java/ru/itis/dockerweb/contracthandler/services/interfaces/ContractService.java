package ru.itis.dockerweb.contracthandler.services.interfaces;

import ru.itis.dockerweb.contracthandler.dto.ContractDto;

/**
 * @author Bulat Giniyatullin
 * 10 November 2018
 */

public interface ContractService {
    void saveContract(ContractDto contractDto);
}
