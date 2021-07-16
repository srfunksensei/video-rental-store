package com.mb.controller;

import com.mb.assembler.resource.rent.RentModel;
import com.mb.dto.CheckInDto;
import com.mb.dto.PriceDto;
import com.mb.service.rent.RentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping(value = "/rents")
@AllArgsConstructor
public class RentController {

	private final RentService rentService;

	@PostMapping(value = "/calculate")
	public ResponseEntity<RentModel> calculate(@RequestBody final CheckInDto rent) {
		return new ResponseEntity<>(rentService.calculate(rent), HttpStatus.OK);
	}

	@PostMapping(value = "/checkIn")
	public ResponseEntity<RentModel> checkIn(@RequestBody final CheckInDto rent) {
		return ResponseEntity.created(URI.create("")).body(rentService.checkIn(rent));
	}

	@PutMapping(value = "/checkOut/{rentId}")
	public ResponseEntity<PriceDto> checkOut(@PathVariable final String rentId, @RequestBody final Set<String> filmIds) {
		return rentService.checkOut(rentId, filmIds)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public HttpEntity<PagedModel<RentModel>> findAll(final Pageable pageable) {
		return new ResponseEntity<>(rentService.findAll(pageable), HttpStatus.OK);
	}

	@GetMapping(value = "/{rentId}")
	public ResponseEntity<RentModel> findOne(@PathVariable final String rentId) {
		return ResponseEntity.ok(rentService.findOne(rentId));
	}

	@DeleteMapping(value = "/{rentId}")
	public ResponseEntity<Void> deleteOne(@PathVariable final String rentId) {
		rentService.deleteOne(rentId);
		return ResponseEntity.noContent().build();
	}
}
