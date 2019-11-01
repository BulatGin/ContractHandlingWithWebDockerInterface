package ru.itis.dockerweb.contracthandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.tarantool.TarantoolClientOps;
import ru.itis.dockerweb.contracthandler.dto.ContractTarantoolDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bulat Giniyatullin
 * 27 November 2018
 */

@Component
public class MyRunner implements CommandLineRunner {
    @Autowired
    private TarantoolClientOps<Integer, List<?>, Object, List<?>> tarantoolClientOps;

    @Autowired
    private ObjectWriter objectWriter;

    @Override
    public void run(String... args) throws Exception {
        List<Object> contract = new ArrayList<>();
        contract.add(2L);
        contract.add(2L);
        contract.add("Contract 2");
        contract.add(2L);
        contract.add(1543320000L);
        tarantoolClientOps.insert(666, contract);
    }
}
