package com.mb.service.customer;

import java.util.Optional;

import com.mb.dto.SearchCustomerDto;
import com.mb.exception.ResourceNotFoundException;
import com.mb.model.customer.Customer_;
import com.mb.repository.GenericQuerySpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;

import com.mb.assembler.resource.customer.CustomerResource;
import com.mb.assembler.resource.customer.CustomerResourceAssemblerSupport;
import com.mb.model.customer.Customer;
import com.mb.repository.customer.CustomerRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;

	private final CustomerResourceAssemblerSupport customerResourceAssembler;
	private final PagedResourcesAssembler<Customer> pagedAssembler;

	public Optional<CustomerResource> findOne(final String customerId) {
		return customerRepository.findById(customerId) //
				.map(customerResourceAssembler::toResource);
	}

	public void deleteOne(final String customerId) {
		if (customerRepository.existsById(customerId)) {
			customerRepository.deleteById(customerId);
		} else {
			throw new ResourceNotFoundException(Customer.class.getTypeName(), customerId);
		}
	}

	public PagedResources<CustomerResource> findAll(final SearchCustomerDto searchCustomerDto, final Pageable pageable) {
		Specification<Customer> spec = null;
		if (searchCustomerDto != null) {
			spec = GenericQuerySpecs.all();
			spec = GenericQuerySpecs.andEqual(spec, Customer_.firstName, searchCustomerDto.getFirstName());
			spec = GenericQuerySpecs.andEqual(spec, Customer_.lastName, searchCustomerDto.getLastName());
			spec = GenericQuerySpecs.andEqual(spec, Customer_.username, searchCustomerDto.getUsername());
			spec = GenericQuerySpecs.andEqual(spec, Customer_.bonusPoints, searchCustomerDto.getBonusPoints());
		}

		final Page<Customer> customers = customerRepository.findAll(spec, pageable);
		return pagedAssembler.toResource(customers, customerResourceAssembler);
	}
}
