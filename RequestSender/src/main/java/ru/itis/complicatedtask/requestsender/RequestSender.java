package ru.itis.complicatedtask.requestsender;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.itis.complicatedtask.requestsender.dto.ContractDto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Bulat Giniyatullin
 * 30 October 2018
 */
@Component
public class RequestSender {
    @Value("${api.server.url}")
    private String url;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    Random random;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Scheduled(fixedDelay = 1000)
    public void sendRequest() throws InterruptedException {
        Collection<Callable<Void>> tasks = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            Callable<Void> task = () -> {
                ContractDto contract = ContractDto.builder()
                        .name(UUID.randomUUID().toString())
                        .description(UUID.randomUUID().toString())
                        .userId((long) random.nextInt(1000000))
                        .interval((long) random.nextInt(1440))
                        .build();
                rabbitTemplate.convertAndSend("contract-request", contract);
                return null;
            };
            tasks.add(task);
        }
        executorService.invokeAll(tasks);
    }
}
