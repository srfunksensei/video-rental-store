package com.mb.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mb.assembler.resource.rent.RentResource;
import com.mb.assembler.resource.rent.RentResourceAssemblerSupport;
import com.mb.dto.CheckInDto;
import com.mb.dto.CheckInItemDto;
import com.mb.dto.PriceDto;
import com.mb.exception.CheckInException;
import com.mb.exception.CheckOutException;
import com.mb.exception.ResourceNotFoundException;
import com.mb.model.rental.Rental;
import com.mb.model.rental.RentalStatus;
import com.mb.service.rent.RentService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RentControllerTest extends AbstractMockMvcTest {

    @MockBean
    private RentService rentService;

    @Autowired
    private RentResourceAssemblerSupport rentResourceAssemblerSupport;

    @Autowired
    private PagedResourcesAssembler<Rental> pagedAssembler;

    @Test
    public void findOne_rentDoesNotExist() throws Exception {
        final String rentId = "not-existing-rent-id";

        when(rentService.findOne(rentId)).thenThrow(ResourceNotFoundException.class);

        mvc.perform(
                get("/rents/{id}", rentId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void findOne() throws Exception {
        final String rentId = "rent-id";

        final RentResource rentResource = new RentResource();
        rentResource.setRentId(rentId);
        rentResource.setFilms(new ArrayList<>());
        rentResource.setStatus(RentalStatus.RETURNED);
        rentResource.setPrice(new PriceDto(BigDecimal.TEN, "SEK"));

        when(rentService.findOne(rentId)).thenReturn(rentResource);

        final MvcResult mvcResult = mvc.perform(
                get("/rents/{id}", rentId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final RentResource result = parseResponse(mvcResult, new TypeReference<RentResource>() {
        });
        Assert.assertNotNull(result);
        Assert.assertEquals(rentResource, result);
    }

    @Test
    public void deleteOne_rentDoesNotExist() throws Exception {
        final String rentId = "not-existing-rent-id";

        doThrow(ResourceNotFoundException.class).when(rentService).deleteOne(eq(rentId));

        mvc.perform(
                delete("/rents/{id}", rentId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteOne() throws Exception {
        final String rentId = "rent-id";

        doNothing().when(rentService).deleteOne(eq(rentId));

        mvc.perform(
                delete("/rents/{id}", rentId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void findAll() throws Exception {
        final Page<Rental> rents = Page.empty();
        final PagedResources<RentResource> rentResources = pagedAssembler.toResource(rents, rentResourceAssemblerSupport);
        when(rentService.findAll(any(Pageable.class))).thenReturn(rentResources);

        final MvcResult mvcResult = mvc.perform(
                get("/rents"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final PagedResources<RentResource> result = parseResponse(mvcResult, new TypeReference<PagedResources<RentResource>>() {
        });

        Assert.assertEquals(0, result.getContent().size());
    }

    @Test
    public void calculate_noItems() throws Exception {
        final CheckInDto checkInDto = new CheckInDto("customerId", new HashSet<>());
        final RentResource toReturn = new RentResource();
        toReturn.setFilms(new ArrayList<>());
        toReturn.setPrice(new PriceDto(new BigDecimal(40), "SEK"));

        when(rentService.calculate(eq(checkInDto))).thenReturn(toReturn);

        final MvcResult mvcResult = mvc.perform(
                post("/rents/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsBytes(checkInDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final RentResource result = parseResponse(mvcResult, new TypeReference<RentResource>() {
        });
        Assert.assertEquals(toReturn, result);
    }

    @Test
    public void checkIn_customerDoesNotExist() throws Exception {
        final CheckInDto checkInDto = new CheckInDto("not-existing-customer-id", new HashSet<>());

        when(rentService.checkIn(eq(checkInDto))).thenThrow(new CheckInException());

        mvc.perform(
                post("/rents/checkIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsBytes(checkInDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkIn_noItems() throws Exception {
        final CheckInDto checkInDto = new CheckInDto("customer-id", new HashSet<>());

        when(rentService.checkIn(eq(checkInDto))).thenThrow(new CheckInException());

        mvc.perform(
                post("/rents/checkIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsBytes(checkInDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkIn_withItems() throws Exception {
        final CheckInItemDto checkInItemDto = new CheckInItemDto(1, "film-id");

        final Set<CheckInItemDto> items = new HashSet<>();
        items.add(checkInItemDto);

        final CheckInDto checkInDto = new CheckInDto("customer-id", items);

        final RentResource toReturn = new RentResource();
        toReturn.setRentId(UUID.randomUUID().toString());
        toReturn.setStatus(RentalStatus.RENTED);
        toReturn.setFilms(new ArrayList<>());
        toReturn.setPrice(new PriceDto(new BigDecimal(40), "SEK"));

        when(rentService.checkIn(eq(checkInDto))).thenReturn(toReturn);

        final MvcResult mvcResult = mvc.perform(
                post("/rents/checkIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsBytes(checkInDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        final RentResource result = parseResponse(mvcResult, new TypeReference<RentResource>() {
        });
        Assert.assertEquals(toReturn, result);
    }

    @Test
    public void checkOut_notExistingRentId() throws Exception {
        final String rentId = "not-existing-rent-id";
        when(rentService.checkOut(eq(rentId), anySet())).thenThrow(new ResourceNotFoundException());

        mvc.perform(
                put("/rents/checkOut/{rentId}", rentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsBytes(new HashSet<>())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkOut_noFilmsReturned() throws Exception {
        final String rentId = "rent-id";
        when(rentService.checkOut(eq(rentId), anySet())).thenThrow(new CheckOutException());

        mvc.perform(
                put("/rents/checkOut/{rentId}", rentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsBytes(new HashSet<>())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkOut() throws Exception {
        final String rentId = "rent-id";

        final PriceDto priceDto = new PriceDto(new BigDecimal(40), "SEK");
        when(rentService.checkOut(eq(rentId), anySet())).thenReturn(Optional.of(priceDto));

        final MvcResult mvcResult = mvc.perform(
                put("/rents/checkOut/{rentId}", rentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsBytes(new HashSet<>())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final PriceDto result = parseResponse(mvcResult, new TypeReference<PriceDto>() {
        });
        Assert.assertEquals(priceDto, result);
    }
}
