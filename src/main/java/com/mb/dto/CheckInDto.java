package com.mb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckInDto {
	private long numOfDays;
	private long filmId;
}
