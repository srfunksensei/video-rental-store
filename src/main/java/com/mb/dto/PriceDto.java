package com.mb.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceDto {
	private BigDecimal value;
	private String currency;
}
