package com.mb.service.customer;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;

import com.mb.assembler.resource.customer.CustomerResource;
import com.mb.assembler.resource.customer.CustomerResourceAssemblerSupport;
import com.mb.model.customer.Customer;
import com.mb.repository.customer.CustomerRepository;

import lombok.AllArgsConstructor;

@Service("customerService")
@AllArgsConstructor
public class CustomerService {

	private final CustomerRepository cutomerRepository;

	private final CustomerResourceAssemblerSupport customerResourceAssembler;
	private final PagedResourcesAssembler<Customer> pagedAssembler;

	public Optional<CustomerResource> findOne(Long customerId) {
		return cutomerRepository.findById(customerId) //
				.map(customerResourceAssembler::toResource);
	}

	public void deleteOne(final Long customerId) {
		cutomerRepository.deleteById(customerId);
	}

	public PagedResources<CustomerResource> findAll(Pageable pageable) {
		final Page<Customer> customers = cutomerRepository.findAll(pageable);
		return pagedAssembler.toResource(customers, customerResourceAssembler);
	}
}
