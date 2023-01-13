package com.tikal.atm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
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
    private Money money;

    @JsonProperty
    private Long amount;

    public static ATMItem of(Money money, Long amount) {
        return ATMItem.builder()
                .money(money)
                .amount(amount)
                .build();
    }

    public Float getMoneyValue() {
        return this.getMoney().getValue();
    }


}
