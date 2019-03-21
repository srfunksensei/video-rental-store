package com.mb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mb.assembler.FilmResourceAssembler;
import com.mb.model.Film;
import com.mb.repository.FilmRepository;

@RestController
@RequestMapping(value = "/films", produces = "application/hal+json")
public class FilmInventoryController {

	private final FilmRepository filmRepository;

	private final FilmResourceAssembler filmResourceAssembler;

	@Autowired
	public FilmInventoryController(final FilmRepository filmRepository,
			final FilmResourceAssembler filmResourceAssembler) {
		this.filmRepository = filmRepository;
		this.filmResourceAssembler = filmResourceAssembler;
	}

	@GetMapping(value = "/{filmId}")
	public ResponseEntity<Resource<Film>> findOne(@PathVariable Long filmId) {
		return filmRepository.findById(filmId) //
				.map(filmResourceAssembler::toResource) //
				.map(ResponseEntity::ok) //
				.orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping(value = "/{filmId}")
	public void deleteOne(@PathVariable Long filmId) {
		filmRepository.deleteById(filmId);
	}

	@GetMapping
	public HttpEntity<PagedResources<Resource<Film>>> findAll(Pageable pageable, PagedResourcesAssembler<Film> pagedAssembler) {
		final Page<Film> films = filmRepository.findAll(pageable);
		return new ResponseEntity<>(pagedAssembler. toResource(films, filmResourceAssembler), HttpStatus.OK);
	}
}
