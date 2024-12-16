package com.moulaMeriame.Product_microservice.controllers;

import com.moulaMeriame.Product_microservice.entities.Product;
import com.moulaMeriame.Product_microservice.services.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService; // Injection du service produit

    @Autowired
    private RestTemplate restTemplate;

    @Value("${server.port}")
    private String port;

    @GetMapping
    @CircuitBreaker(name = "productService", fallbackMethod = "fallback")
    @Retry(name = "myRetry", fallbackMethod = "fallback")
    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallback")
    public List<Product> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        products.addFirst(Product.builder().name("Product service running on port: "+ port).build());
        return products;
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "productService", fallbackMethod = "fallback")
    @Retry(name = "myRetry", fallbackMethod = "fallback")
    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallback")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        // Retourner le produit avec l'ID spécifié
        Product product = productService.getProductById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @CircuitBreaker(name = "productService", fallbackMethod = "fallback")
    @Retry(name = "myRetry", fallbackMethod = "fallback")
    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallback")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // Ajouter un nouveau produit
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }


    @PutMapping("/{id}")
    @CircuitBreaker(name = "productService", fallbackMethod = "fallback")
    @Retry(name = "myRetry", fallbackMethod = "fallback")
    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallback")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        // Mettre à jour un produit existant
        Product updatedProduct = productService.updateProduct(id, product);
        return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    @CircuitBreaker(name = "productService", fallbackMethod = "fallback")
    @Retry(name = "myRetry", fallbackMethod = "fallback")
    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallback")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        // Supprimer le produit avec l'ID spécifié
        boolean isDeleted = productService.deleteProduct(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @GetMapping("/users")
    @CircuitBreaker(name = "productService", fallbackMethod = "fallback")
    @Retry(name = "myRetry", fallbackMethod = "fallback")
    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallback")
    public Object getAllUserss() {
        ResponseEntity<Object> response = restTemplate.exchange(
                "http://USER-SERVER/users",
                HttpMethod.GET,
                null,
                Object.class
        );
        return response.getBody();
    }
    public ResponseEntity<String> fallback(Exception e) {
        return ResponseEntity.status(503).body("Product Service is unavailable. Please try again later!");
    }
}

