package com.mb.service.film;

import com.mb.assembler.resource.film.FilmResource;
import com.mb.assembler.resource.film.FilmResourceAssemblerSupport;
import com.mb.dto.SearchFilmDto;
import com.mb.model.film.Film;
import com.mb.model.film.FilmType;
import com.mb.model.film.Film_;
import com.mb.repository.GenericQuerySpecs;
import com.mb.repository.film.FilmRepository;
import com.mb.repository.film.specification.FilmSpecificationsBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FilmService {
	
	private final FilmRepository filmRepository;
	
	private final FilmResourceAssemblerSupport filmResourceAssembler;
	private final PagedResourcesAssembler<Film> pagedAssembler;
	
	public Optional<FilmResource> findOne(final String filmId) {
		return filmRepository.findById(filmId) //
				.map(filmResourceAssembler::toResource);
	}

	public PagedResources<FilmResource> findAll(final SearchFilmDto searchFilmDto, final Pageable pageable) {
		Specification<Film> spec = null;
		if (searchFilmDto != null) {
			spec = GenericQuerySpecs.all();
			spec = GenericQuerySpecs.andLike(spec, Film_.title, searchFilmDto.getTitle());
			spec = GenericQuerySpecs.andEqual(spec, Film_.type, searchFilmDto.getType());
			spec = GenericQuerySpecs.andEqual(spec, Film_.year, searchFilmDto.getYear());
		}

		final Page<Film> films = filmRepository.findAll(spec, pageable);
		return pagedAssembler.toResource(films, filmResourceAssembler);
	}

	public PagedResources<FilmResource> findAll( //
			final Optional<String> title, final Optional<FilmType> type, //
			 final Pageable pageable) {
		
		final FilmSpecificationsBuilder builder = new FilmSpecificationsBuilder();
		title.ifPresent(s -> builder.with(FilmSpecificationsBuilder.TITLE_SEARCH_KEY, s));
		type.ifPresent(filmType -> builder.with(FilmSpecificationsBuilder.TYPE_SEARCH_KEY, filmType.name()));

		final Page<Film> films = filmRepository.findAll(builder.build(), pageable);
		return pagedAssembler.toResource(films, filmResourceAssembler);
	}
}
