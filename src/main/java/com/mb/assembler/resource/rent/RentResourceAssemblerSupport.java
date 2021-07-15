package com.mb.assembler.resource.rent;

import com.mb.assembler.resource.film.FilmResourceAssemblerSupport;
import com.mb.controller.RentController;
import com.mb.dto.PriceDto;
import com.mb.model.price.Price;
import com.mb.model.rental.Rental;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RentResourceAssemblerSupport extends ResourceAssemblerSupport<Rental, RentResource> {
	
	private final FilmResourceAssemblerSupport filmResourceAssembler;

	public RentResourceAssemblerSupport(final FilmResourceAssemblerSupport filmResourceAssembler) {
		super(RentController.class, RentResource.class);
		this.filmResourceAssembler = filmResourceAssembler;
	}
	
	@Override
	public RentResource toResource(final Rental entity) {
		final RentResource resource = createResourceWithId(entity.getId(), entity);
		resource.setRentId(entity.getId());
		resource.setFilms(entity.getFilms().stream().map(filmResourceAssembler::toResource).collect(Collectors.toList()));
		resource.setPrice(convertPrice(entity.getActualPrice()));
		resource.setStatus(entity.getStatus());
		return resource;
	}

	private PriceDto convertPrice(final Price price) {
		return new PriceDto(price.getValue(), price.getCurrencySymbol());
	}
}
