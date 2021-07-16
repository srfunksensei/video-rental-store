package com.mb.assembler.resource.customer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
public class CustomerModel extends RepresentationModel<CustomerModel> {
	private String customerId;
	private String firstName;
	private String lastName;
	private String username;
	private Long bonusPoints;
}
