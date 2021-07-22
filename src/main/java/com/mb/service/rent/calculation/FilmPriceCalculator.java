package com.mb.service.rent.calculation;

import com.mb.dto.PriceDto;
import com.mb.model.price.Price;
import com.mb.repository.price.PriceBaseRepository;
import lombok.RequiredArgsConstructor;

import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public abstract class FilmPriceCalculator<P extends Price> implements FilmCalculationStrategy {

	private final PriceBaseRepository<P> priceRepository;

	protected PriceDto getBasePrice() {
		final Iterable<P> prices = priceRepository.findAll();
		if (!isUnique(prices)) {
			throw new RuntimeException("Price is not unique!");
		}
		
		final Price price = prices.iterator().next();
		return new PriceDto(price.getValue(), price.getCurrencySymbol());
	}
	
	private boolean isUnique(final Iterable<P> prices) {
		final long pricesCount = StreamSupport.stream(prices.spliterator(), false).count();
		return pricesCount == 1;
	}
}
