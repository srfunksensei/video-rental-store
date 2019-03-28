package com.mb.assembler.resource;

import org.springframework.hateoas.ResourceSupport;

import com.mb.model.FilmType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FilmResource extends ResourceSupport {
	private String title;
	private int year;
	private FilmType type;
}
