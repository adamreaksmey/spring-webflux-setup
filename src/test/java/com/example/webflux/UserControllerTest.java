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
}
