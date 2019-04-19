package com.mb.assembler.resource.customer;

import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerResource extends ResourceSupport {
	private Long customerId;
	private String firstName;
	private String lastName;
	private String username;
	private Long bonusPoints;
}
