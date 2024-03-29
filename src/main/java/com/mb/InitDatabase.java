package com.mb;

import com.mb.model.bonusPoint.BonusPoint;
import com.mb.model.bonusPoint.NewReleaseBonusPoint;
import com.mb.model.bonusPoint.RegularBonusPoint;
import com.mb.model.customer.Customer;
import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import com.mb.model.price.BasicPrice;
import com.mb.model.price.PremiumPrice;
import com.mb.repository.customer.CustomerRepository;
import com.mb.repository.film.FilmRepository;
import com.mb.repository.price.BasicPriceRepository;
import com.mb.repository.price.PremiumPriceRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class InitDatabase {

	private final FilmRepository filmRepository;
	private final BasicPriceRepository basicPriceRepository;
	private final PremiumPriceRepository premiumPriceRepository;
	private final CustomerRepository customerRepository;

	@Bean
	CommandLineRunner init() {	
		final Map<FilmType, BonusPoint> bonusPoints = Stream.of(
			new AbstractMap.SimpleImmutableEntry<>(FilmType.OLD, new RegularBonusPoint(1L)),
			new AbstractMap.SimpleImmutableEntry<>(FilmType.REGULAR, new RegularBonusPoint(1L)),
			new AbstractMap.SimpleImmutableEntry<>(FilmType.NEW, new NewReleaseBonusPoint(2L))
		).collect(Collectors.collectingAndThen(
		    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue), 
		    Collections::<FilmType, BonusPoint> unmodifiableMap));
		
		return args -> {
			final Currency sek = Currency.getInstance(new Locale("sv", "SE"));
			
			basicPriceRepository.save(new BasicPrice(sek.getSymbol(), new BigDecimal(30)));
			premiumPriceRepository.save(new PremiumPrice(sek.getSymbol(), new BigDecimal(40)));
			
			filmRepository.save(new Film("Lord of the rings: The Fellowship of the Ring", 2001, FilmType.OLD, bonusPoints.get(FilmType.OLD)));
			filmRepository.save(new Film("Lord of the rings: The Two Towers", 2002, FilmType.REGULAR, bonusPoints.get(FilmType.REGULAR)));
			filmRepository.save(new Film("Lord of the rings: The Return of the King", 2003, FilmType.NEW, bonusPoints.get(FilmType.NEW)));
			
			customerRepository.save(new Customer("Milan", "Brankovic", "mb", 0L));
		};
	}

}
