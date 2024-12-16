package com.moulaMeriame.User_microservice.controllers;


import com.moulaMeriame.User_microservice.entities.User;
import com.moulaMeriame.User_microservice.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {

    @Value("${server.port}")
    private String port;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;
/*
*   A circuit breaker is a design pattern used to improve the resilience of a system by preventing repeated failures during service outages or high load conditions. It monitors calls between services and can stop unnecessary attempts to reach a service that is unavailable or slow.

    When a service is down or not responding, the circuit breaker "opens" to block further calls temporarily. Once the service appears healthy again, it "closes" to allow calls to resume.*/

    @GetMapping
    @CircuitBreaker(name = "userService", fallbackMethod = "fallback")
    @Retry(name = "myRetry", fallbackMethod = "fallback")
    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallback")
    public List<User> getAllUsers() {
        List<User> users = userService.getAllUsers();
        users.addFirst(User.builder().username("User service running on port: "+ port).build());
        return users;
    }


    @GetMapping("/{id}")
    @CircuitBreaker(name = "userService", fallbackMethod = "fallback")
    @Retry(name = "myRetry", fallbackMethod = "fallback")
    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallback")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @CircuitBreaker(name = "userService", fallbackMethod = "fallback")
    @Retry(name = "myRetry", fallbackMethod = "fallback")
    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallback")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    @CircuitBreaker(name = "userService", fallbackMethod = "fallback")
    @Retry(name = "myRetry", fallbackMethod = "fallback")
    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallback")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @CircuitBreaker(name = "userService", fallbackMethod = "fallback")
    @Retry(name = "myRetry", fallbackMethod = "fallback")
    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallback")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products")
    @CircuitBreaker(name = "userService", fallbackMethod = "fallback")
    @Retry(name = "myRetry", fallbackMethod = "fallback")
    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallback")
    public Object getAllProducts() {
        // Utilisation de RestTemplate pour obtenir tous les produits du service Product
        ResponseEntity<Object> response = restTemplate.exchange(
                "http://PRODUCT-SERVER/products",
                HttpMethod.GET,
                null,
                Object.class
        );
        return response.getBody();
    }




    public ResponseEntity<String> fallback(Exception e) {
        return ResponseEntity.status(503).body("User Service is unavailable. Please try again later!");
    }

}

/*
* attern	Use Case
Circuit Breaker	Protect downstream services from repeated failures and cascading issues.
Retry	Handle transient errors like timeouts or temporary unavailability.
Rate Limiter	Control the load on a service and avoid overloading or abuse.*/