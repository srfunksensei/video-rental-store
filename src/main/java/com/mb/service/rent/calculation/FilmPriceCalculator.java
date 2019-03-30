package com.mb.service.rent.calculation;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.mb.dto.PriceDto;
import com.mb.model.price.Price;
import com.mb.repository.price.PriceBaseRepository;

import lombok.RequiredArgsConstructor;

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
	
	private boolean isUnique(Iterable<P> prices) {
		final Stream<P> pricesStream = StreamSupport.stream(prices.spliterator(), false);
		return pricesStream.count() == 1;
	}
}
