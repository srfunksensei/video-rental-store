package com.mb.assembler.resource.customer;

import com.mb.controller.CustomerController;
import com.mb.model.customer.Customer;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CustomerResourceAssemblerSupport extends RepresentationModelAssemblerSupport<Customer, CustomerModel> {

	public CustomerResourceAssemblerSupport() {
		super(CustomerController.class, CustomerModel.class);
	}
	
	@Override
	public CustomerModel toModel(final Customer entity) {
		final CustomerModel resource = createModelWithId(entity.getId(), entity);
		resource.setCustomerId(entity.getId());
		resource.setFirstName(entity.getFirstName());
		resource.setLastName(entity.getLastName());
		resource.setUsername(entity.getUsername());
		resource.setBonusPoints(entity.getBonusPoints());
		return resource;
	}

}
