package com.mb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mb.assembler.resource.RentResource;
import com.mb.dto.CheckInDto;

@RestController
@RequestMapping(value = "/rents")
public class RentController {
	
	@PostMapping(value = "/calculate")
	public ResponseEntity<RentResource> calculate(@RequestBody CheckInDto rent) {
		return null;
	}
}
