package com.mb.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PriceDto {
	private BigDecimal value;
	private String currency;
}
