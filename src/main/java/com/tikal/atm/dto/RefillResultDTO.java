package com.tikal.atm.dto;

import lombok.*;
@Data
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RefillResultDTO {
    private String id;
    private Long updatedAmount;

    public static RefillResultDTO of(String id, Long amount) {
        return RefillResultDTO.builder()
                .id (id)
                .updatedAmount(amount)
                .build();
    }
}
