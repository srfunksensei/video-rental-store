package com.mb.repository.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mb.model.customer.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Page<Customer> findAll(Pageable pageable);
}
