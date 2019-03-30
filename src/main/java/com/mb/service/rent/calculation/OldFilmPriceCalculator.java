package com.mb.service.rent.calculation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mb.dto.PriceDto;
import com.mb.model.film.FilmType;
import com.mb.model.price.BasicPrice;
import com.mb.repository.price.BasicPriceRepository;

@Component
public class OldFilmPriceCalculator extends FilmPriceCalculator<BasicPrice> {

	public static final long NUM_OF_FIRST_DAYS_WITH_SAME_PRICE = 5;
	
	@Autowired
	public OldFilmPriceCalculator(BasicPriceRepository priceRepository) {
		super(priceRepository);
	}

	@Override
	public PriceDto calculatePrice(long numOfDays) {
		final PriceDto price = getBasePrice();
		
		final BigDecimal value = calculatePrice(price.getValue(), numOfDays);
		return new PriceDto(value, price.getCurrency());
	}
	
	private BigDecimal calculatePrice(final BigDecimal baseValue, long numOfDays) {
		if (numOfDays > OldFilmPriceCalculator.NUM_OF_FIRST_DAYS_WITH_SAME_PRICE) {
			final long dateDiff = numOfDays - OldFilmPriceCalculator.NUM_OF_FIRST_DAYS_WITH_SAME_PRICE;
			return baseValue.add(baseValue.multiply(new BigDecimal(dateDiff)));
		}
		
		return baseValue;
	}

	@Override
	public FilmType getFilmType() {
		return FilmType.OLD;
	}
}
