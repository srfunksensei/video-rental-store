package com.mb.service.rent.calculation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mb.dto.PriceDto;
import com.mb.model.film.FilmType;
import com.mb.model.price.PremiumPrice;
import com.mb.repository.price.PremiumPriceRepository;

@Component
public class NewReleaseFilmPriceCalculator extends FilmPriceCalculator<PremiumPrice> {
	
	@Autowired
	public NewReleaseFilmPriceCalculator(PremiumPriceRepository priceRepository) {
		super(priceRepository);
	}

	@Override
	public PriceDto calculatePrice(long numOfDays) {
		final PriceDto price = getBasePrice();
		return new PriceDto(price.getValue().multiply(new BigDecimal(numOfDays)), price.getCurrency());
	}

	@Override
	public FilmType getFilmType() {
		return FilmType.NEW;
	}
}
