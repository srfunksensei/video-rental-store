package com.mb.assembler.resource.rent;

import com.mb.assembler.resource.film.FilmResourceAssemblerSupport;
import com.mb.controller.RentController;
import com.mb.dto.PriceDto;
import com.mb.model.price.Price;
import com.mb.model.rental.Rental;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RentResourceAssemblerSupport extends RepresentationModelAssemblerSupport<Rental, RentModel> {
	
	private final FilmResourceAssemblerSupport filmResourceAssembler;

	public RentResourceAssemblerSupport(final FilmResourceAssemblerSupport filmResourceAssembler) {
		super(RentController.class, RentModel.class);
		this.filmResourceAssembler = filmResourceAssembler;
	}
	
	@Override
	public RentModel toModel(final Rental entity) {
		final RentModel resource = createModelWithId(entity.getId(), entity);
		resource.setRentId(entity.getId());
		resource.setFilms(entity.getFilms().stream().map(filmResourceAssembler::toModel).collect(Collectors.toList()));
		resource.setPrice(convertPrice(entity.getActualPrice()));
		resource.setStatus(entity.getStatus());
		return resource;
	}

	private PriceDto convertPrice(final Price price) {
		return new PriceDto(price.getValue(), price.getCurrencySymbol());
	}
}
