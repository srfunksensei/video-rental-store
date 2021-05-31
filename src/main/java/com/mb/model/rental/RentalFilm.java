package com.mb.model.rental;

import com.mb.model.AbstractEntity;
import com.mb.model.film.Film;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "rental_film")
@Getter
@Setter
@NoArgsConstructor
public class RentalFilm extends AbstractEntity {
 
    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;
 
    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;
 
    @Column(nullable = false)
    private Long numOfDaysToRent;
    
    @Column(nullable = false)
	@Enumerated(EnumType.STRING)
    private RentalStatus status;
    
    public RentalFilm(final Film film, final Rental rental, final Long numOfDaysToRent) {
		this.film = film;
		this.rental = rental;
		this.numOfDaysToRent = numOfDaysToRent;
		this.status = RentalStatus.RENTED;
	}

	public boolean isOverdue() {
		return createdDate.plusDays(numOfDaysToRent).compareTo(LocalDate.now()) > 0;
	}
	
	public Long getOverdueDays() {
		return getActualRentedDays() - numOfDaysToRent;
	}
	
	public Long getActualRentedDays() {
		return ChronoUnit.DAYS.between(createdDate, LocalDate.now());
	}
}
