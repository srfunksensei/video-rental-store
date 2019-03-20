package com.mb;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.mb.model.Film;
import com.mb.model.FilmType;
import com.mb.repository.FilmRepository;

@Component
public class InitDatabase {

	private final FilmRepository filmRepository;

	InitDatabase(FilmRepository filmRepository) {
		this.filmRepository = filmRepository;
	}

	@Bean
	CommandLineRunner loadEmployees() {
		return args -> {
			filmRepository.save(new Film(1L, LocalDate.now(), LocalDate.now(), "Lord of the rings: The Fellowship of the Ring", 2001, FilmType.OLD));
			filmRepository.save(new Film(2L, LocalDate.now(), LocalDate.now(), "Lord of the rings: The Two Towers", 2002, FilmType.REGULAR));
			filmRepository.save(new Film(3L, LocalDate.now(), LocalDate.now(), "Lord of the rings: The Return of the King", 2003, FilmType.NEW));
		};
	}

}
