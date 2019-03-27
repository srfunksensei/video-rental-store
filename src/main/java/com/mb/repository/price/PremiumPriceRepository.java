package com.mb.repository.price;

import javax.transaction.Transactional;

import com.mb.model.price.PremiumPrice;

@Transactional
public interface PremiumPriceRepository extends PriceBaseRepository<PremiumPrice> {
}
