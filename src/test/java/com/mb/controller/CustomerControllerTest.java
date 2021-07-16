package com.mb.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mb.assembler.resource.customer.CustomerModel;
import com.mb.assembler.resource.customer.CustomerResourceAssemblerSupport;
import com.mb.dto.SearchCustomerDto;
import com.mb.exception.ResourceNotFoundException;
import com.mb.model.customer.Customer;
import com.mb.service.customer.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest extends AbstractMockMvcTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private CustomerResourceAssemblerSupport customerResourceAssembler;

    @Autowired
    private PagedResourcesAssembler<Customer> pagedAssembler;


    @Test
    public void findOne_customerDoesNotExist() throws Exception {
        final String customerId = "not-existing-customer-id";

        when(customerService.findOne(customerId)).thenReturn(Optional.empty());

        mvc.perform(
                get("/customers/{id}", customerId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void findOne() throws Exception {
        final String customerId = "customer-id";

        final CustomerModel customerModel = new CustomerModel();
        customerModel.setBonusPoints(1L);
        customerModel.setCustomerId(customerId);
        customerModel.setFirstName("first-name");
        customerModel.setLastName("last-name");
        customerModel.setUsername("username");

        when(customerService.findOne(customerId)).thenReturn(Optional.of(customerModel));

        final MvcResult mvcResult = mvc.perform(
                get("/customers/{id}", customerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final CustomerModel result = parseResponse(mvcResult, new TypeReference<CustomerModel>() {
        });
        Assertions.assertNotNull(result);
        Assertions.assertEquals(customerModel, result);
    }

    @Test
    public void deleteOne_customerDoesNotExist() throws Exception {
        final String customerId = "not-existing-customer-id";

        doThrow(ResourceNotFoundException.class).when(customerService).deleteOne(eq(customerId));

        mvc.perform(
                delete("/customers/{id}", customerId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteOne() throws Exception {
        final String customerId = "customer-id";

        doNothing().when(customerService).deleteOne(eq(customerId));

        mvc.perform(
                delete("/customers/{id}", customerId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void findAll_byFirstName() throws Exception {
        final String firstName = "first-name";
        final SearchCustomerDto searchCustomerDto = SearchCustomerDto.builder()
                .firstName(firstName)
                .build();

        final Page<Customer> customers = Page.empty();
        final PagedModel<CustomerModel> customerResources = pagedAssembler.toModel(customers, customerResourceAssembler);
        when(customerService.findAll(eq(searchCustomerDto), any(Pageable.class))).thenReturn(customerResources);

        final MvcResult mvcResult = mvc.perform(
                get("/customers")
                        .param("firstName", firstName))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final PagedModel<CustomerModel> result = parseResponse(mvcResult, new TypeReference<PagedModel<CustomerModel>>() {
        });

        Assertions.assertEquals(0, result.getContent().size());
    }
}
