package com.mb.controller;

import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mb.assembler.resource.rent.RentResource;
import com.mb.dto.CheckInDto;
import com.mb.dto.PriceDto;
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
		final RentResource r = rentService.checkIn(rent);
		return new ResponseEntity<>(r, HttpStatus.CREATED);
	}

	@PutMapping(value = "/checkOut/{rentId}")
	public ResponseEntity<PriceDto> checkOut(@PathVariable Long rentId, @RequestBody Set<Long> filmIds) {
		return rentService.checkOut(rentId, filmIds)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public HttpEntity<PagedResources<RentResource>> findAll(Pageable pageable) {
		return new ResponseEntity<>(rentService.findAll(pageable), HttpStatus.OK);
	}

	@GetMapping(value = "/{rentId}")
	public ResponseEntity<RentResource> findOne(@PathVariable Long rentId) {
		return rentService.findOne(rentId) //
				.map(ResponseEntity::ok) //
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(value = "/{rentId}")
	public void deleteOne(@PathVariable Long rentId) {
		rentService.deleteOne(rentId);
	}
}
