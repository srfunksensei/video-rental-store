package com.mb.assembler.resource.customer;

import com.mb.model.customer.Customer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerResourceAssemblerSupportTest {

    @Autowired
    private CustomerResourceAssemblerSupport customerResourceAssemblerSupport;

    @Test
    public void toResource() {
        final Customer customer = new Customer();
        customer.setId("id");
        customer.setFirstName("firstName");
        customer.setLastName("lastName");
        customer.setUsername("username");
        customer.setBonusPoints(1L);

        final CustomerResource result = customerResourceAssemblerSupport.toResource(customer);
        Assert.assertEquals(customer.getId(), result.getCustomerId());
        Assert.assertEquals(customer.getFirstName(), result.getFirstName());
        Assert.assertEquals(customer.getLastName(), result.getLastName());
        Assert.assertEquals(customer.getUsername(), result.getUsername());
        Assert.assertEquals(customer.getBonusPoints(), result.getBonusPoints());

    }
}
