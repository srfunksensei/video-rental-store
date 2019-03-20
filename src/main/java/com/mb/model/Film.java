package com.mb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "film",
	indexes = { //
			@Index(columnList = "title", name = "film_title_idx"), //
			@Index(columnList = "type", name = "film_type_idx") //
	}, 
	uniqueConstraints = { //
			@UniqueConstraint(columnNames = {"title" , "year"}) //
	}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Film extends AbstractEntity {

	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private int year;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private FilmType type;
}
