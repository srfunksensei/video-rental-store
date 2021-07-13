package com.mb.assembler.resource.customer;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mb.controller.CustomerController;
import com.mb.model.customer.Customer;

@Component
public class CustomerResourceAssemblerSupport extends ResourceAssemblerSupport<Customer, CustomerResource>{

	public CustomerResourceAssemblerSupport() {
		super(CustomerController.class, CustomerResource.class);
	}
	
	@Override
	public CustomerResource toResource(final Customer entity) {
		final CustomerResource resource = createResourceWithId(entity.getId(), entity);
		resource.setCustomerId(entity.getId());
		resource.setFirstName(entity.getFirstName());
		resource.setLastName(entity.getLastName());
		resource.setUsername(entity.getUsername());
		resource.setBonusPoints(entity.getBonusPoints());
		return resource;
	}

}
