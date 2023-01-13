package com.tikal.atm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ATMWithdrawalResultWrapperDTO {

    private List<ATMWithdrawalResultDTO> bills;
    private List<ATMWithdrawalResultDTO> coins;

    public static ATMWithdrawalResultWrapperDTO of(List<ATMWithdrawalResultDTO> bills, List<ATMWithdrawalResultDTO> coins) {
        return ATMWithdrawalResultWrapperDTO.builder()
                .bills(bills)
                .coins(coins)
                .build();
    }
}
