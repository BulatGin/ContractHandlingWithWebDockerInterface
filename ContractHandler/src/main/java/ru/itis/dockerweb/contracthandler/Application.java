package ru.itis.dockerweb.contracthandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.tarantool.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * @author Bulat Giniyatullin
 * 10 November 2018
 */

@SpringBootApplication
public class Application implements RabbitListenerConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public MappingJackson2MessageConverter consumerJacksonConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJacksonConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
        rabbitListenerEndpointRegistrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    public SocketChannelProvider socketChannelProvider() {
        return (retryNumber, lastError) -> {
            if (lastError != null) {
                lastError.printStackTrace(System.out);
            }
            try {
                return SocketChannel.open(new InetSocketAddress("localhost", 3301));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        };
    }

    @Bean
    public TarantoolClientConfig tarantoolClientConfig() {
        TarantoolClientConfig config = new TarantoolClientConfig();
        config.username = "guest";
        return config;
    }

    @Bean(destroyMethod = "close")
    public TarantoolClient tarantoolClient(
            SocketChannelProvider socketChannelProvider,
            TarantoolClientConfig config) {
        return new TarantoolClientImpl(socketChannelProvider, config);
    }

    @Bean
    public TarantoolClientOps<Integer, List<?>, Object, List<?>> tarantoolSyncOps(
            TarantoolClient tarantoolClient) {
        return tarantoolClient.syncOps();
    }

    @Bean
    public ObjectWriter objectWriter(ObjectMapper objectMapper) {
        return objectMapper.writer().withDefaultPrettyPrinter();
    }
}
