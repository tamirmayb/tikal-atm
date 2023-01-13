package com.tikal.atm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tikal.atm.utils.Utils;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
    @Indexed(unique = true)
    private String moneyId;

    @JsonProperty
    private Type type;

    @JsonProperty
    private Float value;

    public static Money of(String id, Type type) {
        float parsedValue = Utils.roundFloatStr(id);
        return Money.builder()
                .moneyId(id)
                .type(type)
                .value(parsedValue)
                .build();
    }
}
