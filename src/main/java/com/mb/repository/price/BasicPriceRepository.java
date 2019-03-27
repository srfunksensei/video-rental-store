package com.mb.repository.price;

import javax.transaction.Transactional;

import com.mb.model.price.BasicPrice;

@Transactional
public interface BasicPriceRepository extends PriceBaseRepository<BasicPrice> {
}
