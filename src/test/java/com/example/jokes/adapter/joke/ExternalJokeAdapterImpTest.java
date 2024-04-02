package com.example.jokes.adapter.joke;

import com.example.jokes.exception.ExternalJokeApiClientException;
import com.example.jokes.mapper.ExternalJokeDomainJokeMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponentsBuilder;

import static com.example.jokes.adapter.joke.ExternalJokeAdapterImp.*;
import static com.example.jokes.helper.TestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@ActiveProfiles("test")
@RestClientTest(ExternalJokeAdapterImp.class)
public class ExternalJokeAdapterImpTest {

    @MockBean
    private ExternalJokeDomainJokeMapper externalJokeDomainJokeMapper;
    @Autowired
    private ExternalJokeAdapterImp externalJokeAdapterImp;
    @Autowired
    private MockRestServiceServer server;
    @Autowired
    private ObjectMapper mapper;

    private final String firstUriRequest = UriComponentsBuilder.newInstance()
                                                               .scheme(EXTERNAL_JOKE_API_SCHEME)
                                                               .host(EXTERNAL_JOKE_API_HOST)
                                                               .path(GET_JOKES_ANY_PATH)
                                                               .query(QUERY_PARAMETER_TYPE)
                                                               .query(QUERY_PARAMETER_AMOUNT)
                                                               .buildAndExpand(DEFAULT_TYPE_TO_RETRIEVE, MAX_AMOUNT_JOKES_PER_REQUEST)
                                                               .toUriString();

    private final String secondUriRequest = UriComponentsBuilder.newInstance()
                                                                .scheme(EXTERNAL_JOKE_API_SCHEME)
                                                                .host(EXTERNAL_JOKE_API_HOST)
                                                                .path(GET_JOKES_ANY_PATH)
                                                                .query(QUERY_PARAMETER_TYPE)
                                                                .query(QUERY_PARAMETER_AMOUNT)
                                                                .buildAndExpand(DEFAULT_TYPE_TO_RETRIEVE, 6)
                                                                .toUriString();

    @Test
    void when_getJokes_returnDefaultAmountOfJokes() throws JsonProcessingException {
        this.server.expect(
                requestTo(
                        firstUriRequest
                )
        ).andRespond(
                withSuccess(
                        mapper.writeValueAsString(
                                createExternalJokeApiClientResponseWithAmountJokes(MAX_AMOUNT_JOKES_PER_REQUEST)
                        ),
                        MediaType.APPLICATION_JSON
                )
        );
        this.server.expect(
                requestTo(
                        secondUriRequest
                )
        ).andRespond(
                withSuccess(
                        mapper.writeValueAsString(
                                createExternalJokeApiClientResponseWithAmountJokes(6)
                        ),
                        MediaType.APPLICATION_JSON
                )
        );

        assertEquals(DEFAULT_TOTAL_AMOUNT_TO_RETRIEVE, externalJokeAdapterImp.getJokes().size());
        verify(externalJokeDomainJokeMapper, times(DEFAULT_TOTAL_AMOUNT_TO_RETRIEVE)).externalJokeApiClientJokeToDomainJoke(any());
    }

    @Test
    void whenApiReturn4xx_getJokes_throwsExternalJokeApiClientException() {

        this.server.expect(
                requestTo(
                        firstUriRequest
                )
        ).andRespond(withBadRequest());

        assertThrows(ExternalJokeApiClientException.class, () -> externalJokeAdapterImp.getJokes());
        verifyNoInteractions(externalJokeDomainJokeMapper);
    }

    @Test
    void whenApiReturn5xx_getJokes_throwsException() {

        this.server.expect(
                requestTo(
                        firstUriRequest
                )
        ).andRespond(withServerError());

        assertThrows(ExternalJokeApiClientException.class, () -> externalJokeAdapterImp.getJokes());
        verifyNoInteractions(externalJokeDomainJokeMapper);
    }
}