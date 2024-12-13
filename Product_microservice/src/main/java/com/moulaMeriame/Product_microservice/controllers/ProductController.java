package com.moulaMeriame.Product_microservice.controllers;

import com.moulaMeriame.Product_microservice.entities.Product;
import com.moulaMeriame.Product_microservice.services.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService; // Injection du service produit


    @GetMapping
    @CircuitBreaker(name = "productmicroService")
    public List<Product> getAllProducts() {
        // Retourner tous les produits
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "productmicroService")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        // Retourner le produit avec l'ID spécifié
        Product product = productService.getProductById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @CircuitBreaker(name = "productmicroService")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // Ajouter un nouveau produit
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }


    @PutMapping("/{id}")
    @CircuitBreaker(name = "productmicroService")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        // Mettre à jour un produit existant
        Product updatedProduct = productService.updateProduct(id, product);
        return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    @CircuitBreaker(name = "productmicroService")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        // Supprimer le produit avec l'ID spécifié
        boolean isDeleted = productService.deleteProduct(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

