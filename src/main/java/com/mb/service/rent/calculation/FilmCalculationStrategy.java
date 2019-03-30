package com.mb.service.rent.calculation;

import com.mb.dto.PriceDto;
import com.mb.model.film.FilmType;

public interface FilmCalculationStrategy {
	PriceDto calculatePrice(final long numOfDays);
	FilmType getFilmType();
}
