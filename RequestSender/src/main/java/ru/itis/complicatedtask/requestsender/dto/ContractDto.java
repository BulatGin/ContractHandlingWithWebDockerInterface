package ru.itis.complicatedtask.requestsender.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bulat Giniyatullin
 * 30 October 2018
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDto {
    private String name;

    private String description;

    private Long userId;

    private Long interval;
}
