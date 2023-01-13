package com.tikal.atm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tikal.atm.model.Type;
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
    private Type type;

    @JsonProperty
    private Long amount;

    public static ATMItemDTO of(String moneyId, Type type, Long amount) {
        return ATMItemDTO.builder()
                .moneyId (moneyId)
                .type(type)
                .amount(amount)
                .build();
    }


}
