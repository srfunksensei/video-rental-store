package com.mb.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mb.assembler.resource.FilmResource;
import com.mb.model.FilmType;
import com.mb.service.FilmService;

@RestController
@RequestMapping(value = "/films", produces = "application/hal+json")
public class FilmInventoryController {

	private final FilmService filmService;

	@Autowired
	public FilmInventoryController(final FilmService filmService) {
		this.filmService = filmService;
	}

	@GetMapping(value = "/{filmId}")
	public ResponseEntity<FilmResource> findOne(@PathVariable Long filmId) {
		return filmService.findOne(filmId) //
				.map(ResponseEntity::ok) //
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(value = "/{filmId}")
	public void deleteOne(@PathVariable Long filmId) {
		filmService.deleteOne(filmId);
	}

	@GetMapping
	public HttpEntity<PagedResources<FilmResource>> findAll( //
			@RequestParam("title") Optional<String> title, //
			@RequestParam("type") Optional<FilmType> type, //
			Pageable pageable) {
		return new ResponseEntity<>(filmService.findAll(title, type, pageable), HttpStatus.OK);
	}
}
