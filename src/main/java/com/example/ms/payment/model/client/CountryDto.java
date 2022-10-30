package com.example.ms.payment.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto {
    private String name;
    private BigDecimal remainingLimit;
}
