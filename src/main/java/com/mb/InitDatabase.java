package com.mb;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.mb.model.Film;
import com.mb.model.FilmType;
import com.mb.model.bonusPoint.BonusPoint;
import com.mb.model.bonusPoint.NewReleaseBonusPoint;
import com.mb.model.bonusPoint.RegularBonusPoint;
import com.mb.repository.FilmRepository;

@Component
public class InitDatabase {

	private final FilmRepository filmRepository;

	InitDatabase(FilmRepository filmRepository) {
		this.filmRepository = filmRepository;
	}

	@Bean
	CommandLineRunner loadFilms() {	
		final LocalDate now = LocalDate.now();
		
		final Map<FilmType, BonusPoint> bonusPoints = Stream.of( 
			new AbstractMap.SimpleImmutableEntry<>(FilmType.OLD, new RegularBonusPoint(now, now, 1L)),
			new AbstractMap.SimpleImmutableEntry<>(FilmType.REGULAR, new RegularBonusPoint(now, now, 1L)),
			new AbstractMap.SimpleImmutableEntry<>(FilmType.NEW, new NewReleaseBonusPoint(now, now, 2L))
		).collect(Collectors.collectingAndThen(
		    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue), 
		    Collections::<FilmType, BonusPoint> unmodifiableMap));
		
		return args -> {
			filmRepository.save(new Film(now, now, "Lord of the rings: The Fellowship of the Ring", 2001, FilmType.OLD, bonusPoints.get(FilmType.OLD)));
			filmRepository.save(new Film(now, now, "Lord of the rings: The Two Towers", 2002, FilmType.REGULAR, bonusPoints.get(FilmType.REGULAR)));
			filmRepository.save(new Film(now, now, "Lord of the rings: The Return of the King", 2003, FilmType.NEW, bonusPoints.get(FilmType.NEW)));
		};
	}

}
