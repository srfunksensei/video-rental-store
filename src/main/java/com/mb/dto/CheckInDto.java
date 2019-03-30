package com.mb.dto;

import java.util.List;

import lombok.Data;

@Data
public class CheckInDto {
	private int numOfDays;
	private List<Long> films;
}
