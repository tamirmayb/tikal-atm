package com.tikal.atm.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ATMWithdrawalResultDTO {

    private String id;

    private Long amount;

    public static ATMWithdrawalResultDTO fromDTO(ATMItemDTO dto) {
        return ATMWithdrawalResultDTO.builder()
                .id (dto.getMoneyId())
                .amount(dto.getAmount())
                .build();
    }


}
