package com.mb.repository.price;

import com.mb.model.price.Price;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PriceBaseRepository<T extends Price> extends CrudRepository<T, String> {
}