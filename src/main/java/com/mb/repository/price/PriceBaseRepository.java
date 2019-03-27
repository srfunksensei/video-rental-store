package com.mb.repository.price;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.mb.model.price.Price;

@NoRepositoryBean
public interface PriceBaseRepository<T extends Price> extends CrudRepository<T, Long> {

}
