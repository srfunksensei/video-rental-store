package com.mb.service.customer;

import com.mb.assembler.resource.customer.CustomerResource;
import com.mb.exception.ResourceNotFoundException;
import com.mb.model.customer.Customer;
import com.mb.repository.customer.CustomerRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

    @MockBean
    public CustomerRepository customerRepository;

    @Autowired
    public CustomerService customerService;

    @Test
    public void findOne() {
        final Customer dummyCustomer = buildDummyCustomer();
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(dummyCustomer));

        final Optional<CustomerResource> resultOpt = customerService.findOne("id");
        Assert.assertTrue(resultOpt.isPresent());

        final CustomerResource customerResource = resultOpt.get();
        Assert.assertEquals(dummyCustomer.getId(), customerResource.getCustomerId());
        Assert.assertEquals(dummyCustomer.getFirstName(), customerResource.getFirstName());
        Assert.assertEquals(dummyCustomer.getLastName(), customerResource.getLastName());
        Assert.assertEquals(dummyCustomer.getBonusPoints(), customerResource.getBonusPoints());
    }

    @Test
    public void findOne_notFound() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());

        final Optional<CustomerResource> resultOpt = customerService.findOne("id");
        Assert.assertFalse(resultOpt.isPresent());
    }

    @Test
    public void deleteOne() {
        when(customerRepository.existsById(anyString())).thenReturn(true);

        customerService.deleteOne("id");
        verify(customerRepository, times(1)).deleteById(anyString());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteOne_notFound() {
        when(customerRepository.existsById(anyString())).thenReturn(false);

        customerService.deleteOne("id");
    }

    private Customer buildDummyCustomer() {
        final Customer customer = new Customer();
        customer.setId("id");
        customer.setFirstName("firstName");
        customer.setLastName("lastName");
        customer.setUsername("username");
        customer.setBonusPoints(0L);
        return customer;
    }
}
