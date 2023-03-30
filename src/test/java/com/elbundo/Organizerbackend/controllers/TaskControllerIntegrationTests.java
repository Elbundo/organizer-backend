package com.elbundo.Organizerbackend.controllers;

import com.elbundo.Organizerbackend.dto.AuthenticationRequest;
import com.elbundo.Organizerbackend.dto.TokenPair;
import com.elbundo.Organizerbackend.repositories.UserRepository;
import com.elbundo.Organizerbackend.services.AuthenticationService;
import com.elbundo.Organizerbackend.services.Impl.JwtServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class TaskControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthenticationService authService;
    private TokenPair userTokens;
    private TokenPair adminTokens;
    @BeforeEach
    public void authenticate() {
        userTokens = authService.authenticate(AuthenticationRequest.builder()
                .login("user")
                .password("user")
                .build());
        adminTokens = authService.authenticate(AuthenticationRequest.builder()
                .login("admin")
                .password("admin")
                .build());
    }

    @Test
    public void shouldReturnNewTaskWhenTextIsNotNullAndNotEmpty() throws Exception {
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + userTokens.accessToken())
                        .content(
                                """
                                {
                                    "text": "Do something"
                                }
                                """
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Do something"))
                .andExpect(jsonPath("$.status").value(false));
    }
}
