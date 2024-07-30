package com.webflux.spring_r2dbc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webflux.spring_r2dbc.entity.Product;
import com.webflux.spring_r2dbc.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	private final ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	   @PostMapping
	    public Mono<Product> create(@RequestBody Product product) {
	        return productService.create(product);
	    }
	   
	   @GetMapping
	    public Flux<Product> findAll() {
	        return productService.getAllProducts();
	    }
	   
	   @GetMapping("/{id}")
	    public Mono<ResponseEntity<Product>> findById(@PathVariable Long id) {
	        return productService.getProductById(id)
	                .map(ResponseEntity::ok)
	                .defaultIfEmpty(ResponseEntity.notFound().build());
	    }
	   
	   @DeleteMapping("/{id}")
	    public Mono<ResponseEntity<Void>> deleteById(@PathVariable Long id) {
	        return productService.deleteProduct(id)
	                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
	                .onErrorReturn(ResponseEntity.notFound().build());
	    }
	   
	   @PutMapping("/{id}/price")
	    public Mono<ResponseEntity<Object>> updateProductPrice(@PathVariable Long id, @RequestParam Double newPrice) {
	        return productService.updateProduct(id, newPrice)
	                .then(Mono.just(ResponseEntity.ok().build())) // Return 200 OK on success
	                .onErrorResume(e -> {
	                    // Handle error and return 404 Not Found if the product doesn't exist
	                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	                });
	    }
}
