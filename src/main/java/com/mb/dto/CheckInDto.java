package com.mb.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckInDto {
	private Set<CheckInItemDto> items;
}
