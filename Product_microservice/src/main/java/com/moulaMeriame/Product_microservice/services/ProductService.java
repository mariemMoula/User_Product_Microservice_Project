package com.moulaMeriame.Product_microservice.services;

import com.moulaMeriame.Product_microservice.entities.Product;
import com.moulaMeriame.Product_microservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository; // Injection du repository

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Retourne un produit par son ID
    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null); // Retourne null si le produit n'existe pas
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        if (productRepository.existsById(id)) {
            product.setId(id); // Assurez-vous que l'ID du produit est mis à jour
            return productRepository.save(product);
        }
        return null; // Retourne null si le produit n'existe pas
    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true; // Retourne vrai si la suppression réussit
        }
        return false; // Retourne faux si le produit n'existe pas
    }
}
