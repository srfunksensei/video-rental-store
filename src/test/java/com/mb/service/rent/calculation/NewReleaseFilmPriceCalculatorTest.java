package com.mb.service.rent.calculation;

import com.mb.dto.PriceDto;
import com.mb.exception.InvalidDataException;
import com.mb.model.price.PremiumPrice;
import com.mb.repository.price.PremiumPriceRepository;
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
public class NewReleaseFilmPriceCalculatorTest {

    @MockBean
    public PremiumPriceRepository priceRepository;

    @Autowired
    public NewReleaseFilmPriceCalculator newReleaseFilmPriceCalculator;

    @Test(expected = InvalidDataException.class)
    public void calculatePrice_negativeDays() {
        newReleaseFilmPriceCalculator.calculatePrice(-1L);
    }

    @Test
    public void calculatePrice_zeroDays() {
        final PremiumPrice price = new PremiumPrice();
        price.setValue(BigDecimal.ONE);
        price.setCurrencySymbol("RSD");

        final List<PremiumPrice> list = Collections.singletonList(price);
        when(priceRepository.findAll()).thenReturn(list);

        final PriceDto priceDto = newReleaseFilmPriceCalculator.calculatePrice(0L);

        Assert.assertEquals(BigDecimal.ZERO, priceDto.getValue());
    }

    @Test
    public void calculatePrice_positiveDays() {
        final PremiumPrice price = new PremiumPrice();
        price.setValue(BigDecimal.ONE);
        price.setCurrencySymbol("RSD");

        final List<PremiumPrice> list = Collections.singletonList(price);
        when(priceRepository.findAll()).thenReturn(list);

        final PriceDto priceDto = newReleaseFilmPriceCalculator.calculatePrice(1L);

        Assert.assertEquals(price.getValue(), priceDto.getValue());
    }
}
