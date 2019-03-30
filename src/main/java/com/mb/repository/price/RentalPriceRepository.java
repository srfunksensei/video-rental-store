package com.mb.repository.price;

import javax.transaction.Transactional;

import com.mb.model.price.RentalPrice;

@Transactional
public interface RentalPriceRepository extends PriceBaseRepository<RentalPrice> {
}
