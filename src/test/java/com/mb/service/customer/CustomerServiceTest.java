package com.mb.service.customer;

import com.mb.assembler.resource.customer.CustomerModel;
import com.mb.exception.ResourceNotFoundException;
import com.mb.model.customer.Customer;
import com.mb.repository.customer.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @MockBean
    public CustomerRepository customerRepository;

    @Autowired
    public CustomerService customerService;

    @Test
    public void findOne() {
        final Customer dummyCustomer = buildDummyCustomer();
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(dummyCustomer));

        final Optional<CustomerModel> resultOpt = customerService.findOne("id");
        Assertions.assertTrue(resultOpt.isPresent());

        final CustomerModel customerModel = resultOpt.get();
        Assertions.assertEquals(dummyCustomer.getId(), customerModel.getCustomerId());
        Assertions.assertEquals(dummyCustomer.getFirstName(), customerModel.getFirstName());
        Assertions.assertEquals(dummyCustomer.getLastName(), customerModel.getLastName());
        Assertions.assertEquals(dummyCustomer.getBonusPoints(), customerModel.getBonusPoints());
    }

    @Test
    public void findOne_notFound() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());

        final Optional<CustomerModel> resultOpt = customerService.findOne("id");
        Assertions.assertFalse(resultOpt.isPresent());
    }

    @Test
    public void deleteOne() {
        when(customerRepository.existsById(anyString())).thenReturn(true);

        customerService.deleteOne("id");
        verify(customerRepository, times(1)).deleteById(anyString());
    }

    @Test
    public void deleteOne_notFound() {
        when(customerRepository.existsById(anyString())).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> customerService.deleteOne("id"));
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
