package com.mb.service.rent.calculation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mb.dto.PriceDto;
import com.mb.model.film.FilmType;
import com.mb.model.price.BasicPrice;
import com.mb.repository.price.BasicPriceRepository;

@Component
public class RegularFilmPriceCalculator extends FilmPriceCalculator<BasicPrice> {

	public static final long NUM_OF_FIRST_DAYS_WITH_SAME_PRICE = 3;
	
	@Autowired
	public RegularFilmPriceCalculator(BasicPriceRepository priceRepository) {
		super(priceRepository);
	}

	@Override
	public PriceDto calculatePrice(long numOfDays) {
		final PriceDto price = getBasePrice();
		
		final BigDecimal value = calculatePrice(price.getValue(), numOfDays);
		return new PriceDto(value, price.getCurrency());
	}
	
	private BigDecimal calculatePrice(final BigDecimal baseValue, long numOfDays) {
		if (numOfDays > RegularFilmPriceCalculator.NUM_OF_FIRST_DAYS_WITH_SAME_PRICE) {
			final long dateDiff = numOfDays - RegularFilmPriceCalculator.NUM_OF_FIRST_DAYS_WITH_SAME_PRICE;
			return baseValue.add(baseValue.multiply(new BigDecimal(dateDiff)));
		}
		
		return baseValue;
	}

	@Override
	public FilmType getFilmType() {
		return FilmType.REGULAR;
	}
}
