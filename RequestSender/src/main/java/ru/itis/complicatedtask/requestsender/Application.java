package ru.itis.complicatedtask.requestsender;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

/**
 * @author Bulat Giniyatullin
 * 30 October 2018
 */

@SpringBootApplication
@EnableScheduling
public class Application {
    @Value("${rabbitmq.default-exchange}")
    private String rabbitmqDefaultExchange;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJacksonConverter());
        rabbitTemplate.setExchange(rabbitmqDefaultExchange);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJacksonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
