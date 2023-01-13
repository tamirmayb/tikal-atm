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
@Document(collection = "money")
public class Money {

    @Id
    private
    String id = UUID.randomUUID().toString();

    @JsonProperty
    private String moneyId;

    @JsonProperty
    private Type type;

    @JsonProperty
    private Double value;

    public static Money of(String id, Type type) {
        double parsedValue = Math.round(Float.parseFloat(id) * 100.0) / 100.0;
        return Money.builder()
                .moneyId (id)
                .type (type)
                .value(parsedValue)
                .build();
    }
}
