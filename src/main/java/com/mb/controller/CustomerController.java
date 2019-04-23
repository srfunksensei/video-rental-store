package com.mb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mb.assembler.resource.customer.CustomerResource;
import com.mb.service.customer.CustomerService;

@RestController
@RequestMapping(value = "/customers", produces = "application/hal+json")
public class CustomerController {
	
	private final CustomerService customerService;

	@Autowired
	public CustomerController(final CustomerService customerService) {
		this.customerService = customerService;
	}
	
	@GetMapping(value = "/{customerId}")
	public ResponseEntity<CustomerResource> findOne(@PathVariable Long customerId) {
		return customerService.findOne(customerId) //
				.map(ResponseEntity::ok) //
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(value = "/{customerId}")
	public void deleteOne(@PathVariable Long customerId) {
		customerService.deleteOne(customerId);
	}
	
	@GetMapping
	public HttpEntity<PagedResources<CustomerResource>> findAll(Pageable pageable) {
		return new ResponseEntity<>(customerService.findAll(pageable), HttpStatus.OK);
	}
}
