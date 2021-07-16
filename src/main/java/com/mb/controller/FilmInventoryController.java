package com.mb.controller;

import com.mb.assembler.resource.film.FilmModel;
import com.mb.dto.SearchFilmDto;
import com.mb.model.film.FilmType;
import com.mb.service.film.FilmService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/films", produces = "application/hal+json")
public class FilmInventoryController {

	private final FilmService filmService;

	@GetMapping(value = "/{filmId}")
	public ResponseEntity<FilmModel> findOne(@PathVariable final String filmId) {
		return filmService.findOne(filmId) //
				.map(ResponseEntity::ok) //
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public HttpEntity<PagedModel<FilmModel>> findAll( //
													  @RequestParam(value = "title", required = false) final String title, //
													  @RequestParam(value = "type", required = false) final FilmType type, //
													  @RequestParam(value = "year", required = false) final Integer year, //
													  final Pageable pageable) {
		final SearchFilmDto searchFilmDto = SearchFilmDto.builder()
				.title(title)
				.type(type)
				.year(year)
				.build();
		return new ResponseEntity<>(filmService.findAll(searchFilmDto, pageable), HttpStatus.OK);
	}
}
