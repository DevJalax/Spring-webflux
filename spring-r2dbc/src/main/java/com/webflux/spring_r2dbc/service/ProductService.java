package com.webflux.spring_r2dbc.service;

import org.springframework.stereotype.Service;

import com.webflux.spring_r2dbc.entity.Product;
import com.webflux.spring_r2dbc.repo.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	
	 public ProductService(ProductRepository productRepository) {
	        this.productRepository = productRepository;
	    }
	 
	 // [create or save the request]
	    public Mono<Product> create(Product product)
	    {
	     return productRepository.save(product);
	    }
	    
	    // [get list of all products]
	    public Flux<Product> getAllProducts() 
	    {
	     return productRepository.findAll();
	    }
	    
	    // [get only a product with id]
	    public Mono<Product> getProductById(Long id)
	    {
	     return productRepository.findById(id);
	    }
	    
	    // [delete a product with id]
	    public Mono<Void> deleteProduct(Long id)
	    {
	     return productRepository.deleteById(id);
	    }
	    
	    // [update a product's price with id]
	    public Mono<Void> updateProduct(Long id, Double newPrice) {
	        return productRepository.findById(id) 
	                .flatMap(product -> {
	                    product.setPrice(newPrice); 
	                    return productRepository.save(product); 
	                })
	                .then(); 
	    }
	
}
