package com.example.jokes.it;

import com.example.jokes.adapter.joke.model.ExternalJokeApiClientResponse;
import com.example.jokes.controller.JokesController;
import com.example.jokes.model.DTOJoke;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;

import static com.example.jokes.adapter.joke.ExternalJokeAdapterImp.MAX_AMOUNT_JOKES_PER_REQUEST;
import static com.example.jokes.helper.TestHelper.createExternalJokeApiClientJoke;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class NsAssessmentIntegrationTest {

    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
                                                               .options(wireMockConfig().dynamicPort())
                                                               .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("external-joke-api.base-url", wireMockServer::baseUrl);
    }

    @Autowired
    private JokesController controller;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void given_whenControllerGetJoke_returnSmallestValidJoke() throws JsonProcessingException {
        int expectedId = 3;
        String expectedJokeText = "123";
        wireMockServer.stubFor(
                WireMock.get("/joke/Any?type=single&amount=10")
                        .willReturn(aResponse()
                                            .withHeader(CONTENT_TYPE_HEADER, MediaType.APPLICATION_JSON_VALUE)
                                            .withBody(
                                                    mapper.writeValueAsString(
                                                            new ExternalJokeApiClientResponse(
                                                                    "error",
                                                                    MAX_AMOUNT_JOKES_PER_REQUEST,
                                                                    List.of(
                                                                            createExternalJokeApiClientJoke(1, "1", false, false, false, false),
                                                                            createExternalJokeApiClientJoke(2, "12", false, true, false, true),
                                                                            createExternalJokeApiClientJoke(expectedId, expectedJokeText, false, false, false, true),
                                                                            createExternalJokeApiClientJoke(4, "12345", false, false, false, true),
                                                                            createExternalJokeApiClientJoke(5, "123456", false, false, false, true),
                                                                            createExternalJokeApiClientJoke(6, "1234567", false, false, false, true),
                                                                            createExternalJokeApiClientJoke(7, "123", true, false, false, true),
                                                                            createExternalJokeApiClientJoke(8, "123456", true, false, false, true),
                                                                            createExternalJokeApiClientJoke(9, "12765", true, false, false, true),
                                                                            createExternalJokeApiClientJoke(10, "176572", true, false, false, true)
                                                                    )
                                                            )
                                                    )
                                            )
                        )
        );
        wireMockServer.stubFor(
                WireMock.get("/joke/Any?type=single&amount=6")
                        .willReturn(aResponse()
                                            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                            .withBody(
                                                    mapper.writeValueAsString(
                                                            new ExternalJokeApiClientResponse(
                                                                    "error",
                                                                    MAX_AMOUNT_JOKES_PER_REQUEST,
                                                                    List.of(
                                                                            createExternalJokeApiClientJoke(11, "1", false, false, false, false),
                                                                            createExternalJokeApiClientJoke(12, "12", false, true, false, true),
                                                                            createExternalJokeApiClientJoke(13, "123454689", false, false, false, true),
                                                                            createExternalJokeApiClientJoke(14, "12345", false, false, false, true),
                                                                            createExternalJokeApiClientJoke(15, "123456", false, false, false, true),
                                                                            createExternalJokeApiClientJoke(16, "1234567", false, false, false, true)
                                                                    )
                                                            )
                                                    )
                                            )
                        )
        );

        DTOJoke actual = controller.getJoke();

        assertAll(
                () -> assertEquals(expectedId, actual.id()),
                () -> assertEquals(expectedJokeText, actual.randomJoke())
        );
    }
}
