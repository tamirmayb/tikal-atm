package com.tikal.atm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ATMItemDTO {

    @Id
    private String moneyId;

    @JsonProperty
    private int amount;

    public static ATMItemDTO of(String moneyId, int amount) {
        return ATMItemDTO.builder()
                .moneyId (moneyId)
                .amount(amount)
                .build();
    }


}
