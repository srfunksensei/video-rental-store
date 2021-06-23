package com.mb.service.rent.calculation;

import com.mb.dto.PriceDto;
import com.mb.exception.InvalidDataException;
import com.mb.model.price.BasicPrice;
import com.mb.repository.price.BasicPriceRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegularFilmPriceCalculatorTest {

    @MockBean
    public BasicPriceRepository basicPriceRepository;

    @Autowired
    public RegularFilmPriceCalculator regularFilmPriceCalculator;

    @Test(expected = InvalidDataException.class)
    public void calculatePrice_negativeDays() {
        regularFilmPriceCalculator.calculatePrice(-1L);
    }

    @Test
    public void calculatePrice_samePriceForNDays() {
        final BasicPrice price = new BasicPrice();
        price.setValue(BigDecimal.ONE);
        price.setCurrencySymbol("RSD");

        final List<BasicPrice> list = Collections.singletonList(price);
        when(basicPriceRepository.findAll()).thenReturn(list);

        final long numOfDays = RegularFilmPriceCalculator.NUM_OF_FIRST_DAYS_WITH_SAME_PRICE - 2;
        final PriceDto priceDto = regularFilmPriceCalculator.calculatePrice(numOfDays);

        Assert.assertEquals(price.getValue(), priceDto.getValue());
    }

    @Test
    public void calculatePrice_differentPriceAfterNDays() {
        final BasicPrice price = new BasicPrice();
        price.setValue(BigDecimal.ONE);
        price.setCurrencySymbol("RSD");

        final List<BasicPrice> list = Collections.singletonList(price);
        when(basicPriceRepository.findAll()).thenReturn(list);

        final long numOfDays = RegularFilmPriceCalculator.NUM_OF_FIRST_DAYS_WITH_SAME_PRICE + 2;
        final PriceDto priceDto = regularFilmPriceCalculator.calculatePrice(numOfDays);

        final long diff = numOfDays - RegularFilmPriceCalculator.NUM_OF_FIRST_DAYS_WITH_SAME_PRICE;
        Assert.assertEquals(price.getValue().add(price.getValue().multiply(new BigDecimal(diff))), priceDto.getValue());
    }
}
