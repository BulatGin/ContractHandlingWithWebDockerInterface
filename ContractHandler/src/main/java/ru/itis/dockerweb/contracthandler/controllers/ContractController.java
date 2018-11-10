package ru.itis.dockerweb.contracthandler.controllers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.dockerweb.contracthandler.dto.ContractDto;
import ru.itis.dockerweb.contracthandler.services.interfaces.ContractService;

/**
 * @author Bulat Giniyatullin
 * 10 November 2018
 */

@RestController
public class ContractController {
    @Autowired
    private ContractService contractService;

    @RabbitListener(queues = "contracts-requests")
    void saveContract(ContractDto contractDto) {
        contractService.saveContract(contractDto);
    }
}
