package com.mb.assembler.resource.customer;

import com.mb.model.customer.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomerResourceAssemblerSupportTest {

    @Autowired
    private CustomerResourceAssemblerSupport customerResourceAssemblerSupport;

    @Test
    public void toModel() {
        final Customer customer = new Customer();
        customer.setId("id");
        customer.setFirstName("firstName");
        customer.setLastName("lastName");
        customer.setUsername("username");
        customer.setBonusPoints(1L);

        final CustomerModel result = customerResourceAssemblerSupport.toModel(customer);
        Assertions.assertEquals(customer.getId(), result.getCustomerId());
        Assertions.assertEquals(customer.getFirstName(), result.getFirstName());
        Assertions.assertEquals(customer.getLastName(), result.getLastName());
        Assertions.assertEquals(customer.getUsername(), result.getUsername());
        Assertions.assertEquals(customer.getBonusPoints(), result.getBonusPoints());

    }
}
