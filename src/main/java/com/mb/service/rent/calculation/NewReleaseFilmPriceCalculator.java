package com.mb.service.rent.calculation;

import com.mb.dto.PriceDto;
import com.mb.exception.InvalidDataException;
import com.mb.model.film.FilmType;
import com.mb.model.price.PremiumPrice;
import com.mb.repository.price.PremiumPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class NewReleaseFilmPriceCalculator extends FilmPriceCalculator<PremiumPrice> {
	
	@Autowired
	public NewReleaseFilmPriceCalculator(final PremiumPriceRepository priceRepository) {
		super(priceRepository);
	}

	@Override
	public PriceDto calculatePrice(final long numOfDays) {
		if (numOfDays < 0) {
			throw new InvalidDataException();
		}

		final PriceDto price = getBasePrice();
		return new PriceDto(price.getValue().multiply(new BigDecimal(numOfDays)), price.getCurrency());
	}

	@Override
	public FilmType getFilmType() {
		return FilmType.NEW;
	}
}
