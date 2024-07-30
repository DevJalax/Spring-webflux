package com.webflux.spring_r2dbc.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.webflux.spring_r2dbc.entity.Product;

public interface ProductRepository extends ReactiveCrudRepository<Product,Long> {

}
