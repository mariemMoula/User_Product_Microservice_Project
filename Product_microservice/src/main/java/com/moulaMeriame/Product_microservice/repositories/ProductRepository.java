package com.moulaMeriame.Product_microservice.repositories;

import com.moulaMeriame.Product_microservice.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
