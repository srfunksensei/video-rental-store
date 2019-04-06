package com.mb.repository.rent;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mb.model.rental.Rental;

@Repository("rentRepository")
public interface RentRepository extends CrudRepository<Rental, Long> {
}
