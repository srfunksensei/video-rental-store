package com.mb.repository.rent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mb.model.rental.Rental;

@Repository
public interface RentRepository extends JpaRepository<Rental, Long> {
	
	Page<Rental> findAll(Pageable pageable);
}
