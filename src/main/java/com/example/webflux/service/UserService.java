package com.example.webflux.service;

import org.springframework.stereotype.Service;

import com.example.webflux.model.User;
import com.example.webflux.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> getUserById(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("User not found with id: " + id)));
    }

    public Mono<User> createUser(User user) {
        return userRepository.findByEmail(user.getEmail())
                // If user exists, throw error
                .flatMap(existingUser -> Mono.<User>error(
                        new RuntimeException("Email already in use")))
                // Otherwise, save the user
                .switchIfEmpty(userRepository.save(user));
    }

    public Mono<User> updateUser(String id, User user) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("User not found")))
                .flatMap(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setRole(user.getRole());
                    return userRepository.save(existingUser);
                });
    }

    public Mono<Void> deleteUser(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("User not found")))
                .flatMap(userRepository::delete);
    }

}
