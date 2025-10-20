package com.example.webflux;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.webflux.model.User;
import com.example.webflux.repository.UserRepository;

import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
public class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetAllUsers() {
        webTestClient.get()
                .uri("/api/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .hasSize(0); // Assuming empty DB
    }

    @Test
    void testCreateAndRetrieveUser() {
        User newUser = new User(null, "Alice", "alice@example.com", "USER");
        User savedUser = new User("123", "Alice", "alice@example.com", "USER");

        Mockito.when(userRepository.findByEmail("alice@example.com"))
                .thenReturn(Mono.empty()); // Email doesn't exist yet

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(Mono.just(savedUser));

        webTestClient.post()
                .uri("/api/users")
                .bodyValue(newUser)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(User.class)
                .value(user -> {
                    assert user.getName().equals("Alice");
                    assert user.getId() != null;
                });
    }

    @Test
    void testCreateUserWithDuplicateEmail() {
        User newUser = new User(null, "Bob", "bob@example.com", "USER");
        User existingUser = new User("456", "Robert", "bob@example.com", "USER");

        Mockito.when(userRepository.findByEmail("bob@example.com"))
                .thenReturn(Mono.just(existingUser)); // Email already exists

        webTestClient.post()
                .uri("/api/users")
                .bodyValue(newUser)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").exists();
    }

    @Test
    void testGetUserNotFound() {
        Mockito.when(userRepository.findById("nonexistent"))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/users/nonexistent")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").exists();
    }

    @Test
    void testUpdateUser() {
        User existingUser = new User("789", "Charlie", "charlie@example.com", "USER");
        User updatedUser = new User("789", "Charles", "charles@example.com", "ADMIN");

        Mockito.when(userRepository.findById("789"))
                .thenReturn(Mono.just(existingUser));

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(Mono.just(updatedUser));

        webTestClient.put()
                .uri("/api/users/789")
                .bodyValue(updatedUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> {
                    assert user.getName().equals("Charles");
                    assert user.getRole().equals("ADMIN");
                });
    }

    @Test
    void testDeleteUser() {
        User userToDelete = new User("999", "Dave", "dave@example.com", "USER");

        Mockito.when(userRepository.findById("999"))
                .thenReturn(Mono.just(userToDelete));

        Mockito.when(userRepository.delete(Mockito.any(User.class)))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/users/999")
                .exchange()
                .expectStatus().isNoContent();
    }
}