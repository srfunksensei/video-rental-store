package com.mb.controller;

import com.mb.assembler.resource.customer.CustomerResource;
import com.mb.dto.SearchCustomerDto;
import com.mb.service.customer.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/customers", produces = "application/hal+json")
public class CustomerController {
	
	private final CustomerService customerService;

	@GetMapping(value = "/{customerId}")
	public ResponseEntity<CustomerResource> findOne(@PathVariable final String customerId) {
		return customerService.findOne(customerId) //
				.map(ResponseEntity::ok) //
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(value = "/{customerId}")
	public ResponseEntity<Void> deleteOne(@PathVariable final String customerId) {
		customerService.deleteOne(customerId);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	public HttpEntity<PagedResources<CustomerResource>> findAll(@RequestParam(value = "firstName", required = false) final String firstName,
																@RequestParam(name = "lastName", required = false) final String lastName,
																@RequestParam(name = "username", required = false) final String username,
																@RequestParam(name = "bonusPoints", required = false) final Long bonusPoints,
																final Pageable pageable) {
		final SearchCustomerDto searchCustomerDto = SearchCustomerDto.builder()
				.firstName(firstName)
				.lastName(lastName)
				.username(username)
				.bonusPoints(bonusPoints)
				.build();
		return new ResponseEntity<>(customerService.findAll(searchCustomerDto, pageable), HttpStatus.OK);
	}
}
