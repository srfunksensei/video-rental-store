package com.mb.assembler.resource.film;

import com.mb.controller.FilmInventoryController;
import com.mb.model.film.Film;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class FilmResourceAssemblerSupport extends ResourceAssemblerSupport<Film, FilmResource> {

	public FilmResourceAssemblerSupport() {
		super(FilmInventoryController.class, FilmResource.class);
	}

	@Override
	public FilmResource toResource(final Film entity) {
		final FilmResource resource = createResourceWithId(entity.getId(), entity);
		resource.setFilmId(entity.getId());
		resource.setTitle(entity.getTitle());
		resource.setYear(entity.getYear());
		resource.setType(entity.getType());
		return resource;
	}

}
