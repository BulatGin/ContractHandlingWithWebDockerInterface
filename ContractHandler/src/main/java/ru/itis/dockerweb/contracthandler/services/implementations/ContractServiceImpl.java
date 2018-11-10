package ru.itis.dockerweb.contracthandler.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import ru.itis.dockerweb.contracthandler.dto.ContractDto;
import ru.itis.dockerweb.contracthandler.models.Contract;
import ru.itis.dockerweb.contracthandler.repositories.ContractRepository;
import ru.itis.dockerweb.contracthandler.services.interfaces.ContractService;

import javax.annotation.Resource;

/**
 * @author Bulat Giniyatullin
 * 10 November 2018
 */

@Service
public class ContractServiceImpl implements ContractService {
    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void saveContract(ContractDto contractDto) {
        Contract contract = Contract.builder()
                .name(contractDto.getName())
                .description(contractDto.getDescription())
                .userId(contractDto.getUserId())
                .interval(contractDto.getInterval())
                .build();

        contract = contractRepository.saveAndFlush(contract);

        redisTemplate.opsForValue().set(String.valueOf(contract.getId()),
                String.valueOf(contract.getUserId()));
    }
}
