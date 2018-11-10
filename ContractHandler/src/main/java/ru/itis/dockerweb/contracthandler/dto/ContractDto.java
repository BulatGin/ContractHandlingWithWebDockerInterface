package ru.itis.dockerweb.contracthandler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Bulat Giniyatullin
 * 10 November 2018
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDto implements Serializable {
    private String name;

    private String description;

    private Long userId;

    private Long interval;
}
