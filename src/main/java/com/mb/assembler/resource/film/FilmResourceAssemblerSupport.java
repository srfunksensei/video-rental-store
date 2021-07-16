package com.mb.assembler.resource.film;

import com.mb.controller.FilmInventoryController;
import com.mb.model.film.Film;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class FilmResourceAssemblerSupport extends RepresentationModelAssemblerSupport<Film, FilmModel> {

	public FilmResourceAssemblerSupport() {
		super(FilmInventoryController.class, FilmModel.class);
	}

	@Override
	public FilmModel toModel(final Film entity) {
		final FilmModel resource = createModelWithId(entity.getId(), entity);
		resource.setFilmId(entity.getId());
		resource.setTitle(entity.getTitle());
		resource.setYear(entity.getYear());
		resource.setType(entity.getType());
		return resource;
	}

}
