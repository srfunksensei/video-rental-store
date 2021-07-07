package com.mb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class CheckInDto {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String customerId;
	private Set<CheckInItemDto> items;
}
