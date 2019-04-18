package com.mb.assembler.resource.rent;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mb.assembler.resource.film.FilmResource;
import com.mb.dto.PriceDto;
import com.mb.model.rental.RentalStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RentResource extends ResourceSupport {
	@JsonInclude(Include.NON_NULL)
	private Long rentId;
	private List<FilmResource> films;
	private PriceDto price;
	@JsonInclude(Include.NON_NULL)
	private RentalStatus status;
}
