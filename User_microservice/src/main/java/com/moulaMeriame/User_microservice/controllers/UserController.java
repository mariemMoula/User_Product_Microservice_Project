package com.moulaMeriame.User_microservice.controllers;


import com.moulaMeriame.User_microservice.entities.User;
import com.moulaMeriame.User_microservice.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
/*
*   A circuit breaker is a design pattern used to improve the resilience of a system by preventing repeated failures during service outages or high load conditions. It monitors calls between services and can stop unnecessary attempts to reach a service that is unavailable or slow.

    When a service is down or not responding, the circuit breaker "opens" to block further calls temporarily. Once the service appears healthy again, it "closes" to allow calls to resume.*/

    @GetMapping
    @CircuitBreaker(name = "usermicroService")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{id}")
    @CircuitBreaker(name = "usermicroService")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @CircuitBreaker(name = "usermicroService")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    @CircuitBreaker(name = "usermicroService", fallbackMethod = "fallback")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @CircuitBreaker(name = "usermicroService")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    public void fallback(Exception e) {
        System.out.println("Service is unavailable. Please try again later!"); ;
    }
}

