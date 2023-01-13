package com.tikal.atm.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ATMWithdrawalResultDTO {

    private String id;

    private int amount;

    public static ATMWithdrawalResultDTO fromDTO(ATMItemDTO dto) {
        return ATMWithdrawalResultDTO.builder()
                .id (dto.getMoneyId())
                .amount(dto.getAmount())
                .build();
    }


}
