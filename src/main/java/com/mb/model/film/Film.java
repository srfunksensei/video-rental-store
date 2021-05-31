package com.mb.model.film;

import com.mb.model.AbstractEntity;
import com.mb.model.bonusPoint.BonusPoint;
import com.mb.model.rental.RentalFilm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
	
	@OneToMany(mappedBy = "film")
    private Set<RentalFilm> rentals = new HashSet<>();
	
	public Film(final String title, final int year, final FilmType type, final BonusPoint bonus) {
		this.title = title;
		this.year = year;
		this.type = type;
		this.bonus = bonus;
	}
	
	public Set<RentalFilm> addRental(final RentalFilm rental) {
		rentals.add(rental);
		return rentals;
	}
}
