package com.mb.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mb.assembler.resource.RentResource;
import com.mb.dto.CheckInDto;
import com.mb.service.rent.RentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/rents")
@AllArgsConstructor
public class RentController {
	
	private final RentService rentService;
	
	@PostMapping(value = "/calculate")
	public ResponseEntity<RentResource> calculate(@RequestBody Set<CheckInDto> rent) {
		return new ResponseEntity<>(rentService.calculate(rent), HttpStatus.OK);
	}
	
	@PostMapping(value = "/checkIn")
	public ResponseEntity<RentResource> checkIn(@RequestBody Set<CheckInDto> rent) {
		return rentService.checkIn(rent) //
				.map(ResponseEntity::ok) //
				.orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}
}
