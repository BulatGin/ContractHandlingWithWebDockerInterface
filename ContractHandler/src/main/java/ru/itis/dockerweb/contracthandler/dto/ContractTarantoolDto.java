package ru.itis.dockerweb.contracthandler.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Bulat Giniyatullin
 * 27 November 2018
 */

@Data
@Builder
public class ContractTarantoolDto {
    private Long id;
    private Long contract_id;
    private String contract_name;
    private Long user_id;
    private Long expiration_timestamp;
}
