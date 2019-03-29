package com.mb.model.film;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.mb.model.AbstractEntity;
import com.mb.model.bonusPoint.BonusPoint;

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
public class Film extends AbstractEntity {
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private int year;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private FilmType type;
	
	@JoinColumn(name = "BONUS_POINT_ID")
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private BonusPoint bonus;
	
	public Film(LocalDate createdDate, LocalDate updatedDate, String title, int year, FilmType type, BonusPoint bonus) {
		super(createdDate, updatedDate);
		this.title = title;
		this.year = year;
		this.type = type;
		this.bonus = bonus;
	}

}
