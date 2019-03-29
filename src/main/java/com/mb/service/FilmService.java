package com.mb.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;

import com.mb.assembler.FilmResourceAssemblerSupport;
import com.mb.assembler.resource.FilmResource;
import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import com.mb.repository.FilmRepository;
import com.mb.repository.specification.FilmSpecificationsBuilder;

import lombok.AllArgsConstructor;

@Service("filmService")
@AllArgsConstructor
public class FilmService {
	
	private final FilmRepository filmRepository;
	
	private final FilmResourceAssemblerSupport filmResourceAssembler;
	private final PagedResourcesAssembler<Film> pagedAssembler;
	
	public Optional<FilmResource> findOne(Long filmId) {
		return filmRepository.findById(filmId) //
				.map(filmResourceAssembler::toResource);
	}

	public void deleteOne(final Long id) {
		filmRepository.deleteById(id);
	}
	
	public PagedResources<FilmResource> findAll( //
			Optional<String> title, Optional<FilmType> type, //
			Pageable pageable) {
		
		final FilmSpecificationsBuilder builder = new FilmSpecificationsBuilder();
		if (title.isPresent()) {
			builder.with(FilmSpecificationsBuilder.TITLE_SEARCH_KEY, title.get());
		}
		if (type.isPresent()) {
			builder.with(FilmSpecificationsBuilder.TYPE_SEARCH_KEY, type.get().name());
		}

		final Page<Film> films = filmRepository.findAll(builder.build(), pageable);
		return pagedAssembler.toResource(films, filmResourceAssembler);
	}
}
