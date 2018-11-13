package ru.itis.dockerweb.managmentservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author Bulat Giniyatullin
 * 11 November 2018
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("Replica")
public class Replica {
    @Id
    private String containerId;

    private Status status;

    private Long memoryUsage;

    private Double cpu;
}
