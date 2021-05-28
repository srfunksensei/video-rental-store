package com.mb.dto;

import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckInDto {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long customerId;
	private Set<CheckInItemDto> items;
}
