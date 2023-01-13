package com.tikal.atm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "atm")
public class ATMItem {

    @Id
    private
    String id = UUID.randomUUID().toString();

    @JsonProperty
    private String moneyId;

    @JsonProperty
    private int amount;

    public static ATMItem of(String moneyId, int amount) {
        return ATMItem.builder()
                .moneyId (moneyId)
                .amount(amount)
                .build();
    }


}
