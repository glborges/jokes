package com.example.jokes.adapter.joke;

import com.example.jokes.adapter.joke.model.ExternalJokeApiClientResponse;
import com.example.jokes.exception.ExternalJokeApiClientException;
import com.example.jokes.mapper.ExternalJokeDomainJokeMapper;
import com.example.jokes.model.DomainJoke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ExternalJokeAdapterImp implements ExternalJokeAdapter {

    public static final int DEFAULT_TOTAL_AMOUNT_TO_RETRIEVE = 16;
    public static final String DEFAULT_TYPE_TO_RETRIEVE = "single";
    public static final int MAX_AMOUNT_JOKES_PER_REQUEST = 10;
    public static final String GET_JOKES_ANY_PATH = "/joke/Any";
    public static final String QUERY_PARAMETER_TYPE = "type={type}";
    public static final String QUERY_PARAMETER_AMOUNT = "amount={amount}";
    public static final String ERROR_WHILE_CALLING_EXTERNAL_JOKE_API_STATUS_S_AND_MESSAGE_S = "Error while calling external joke api status: %s and message %s";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String LOG_ERROR_MESSAGE = "Error while getting jokes from external api. " +
            "Request info: URI {} ." +
            "Response info: code {} message {}";
    private final ExternalJokeDomainJokeMapper mapper;
    private final RestClient restClient;
    Logger logger = LoggerFactory.getLogger(ExternalJokeAdapterImp.class);

    public ExternalJokeAdapterImp(ExternalJokeDomainJokeMapper mapper, RestClient.Builder builder, @Value("${external-joke-api.base-url}") String baseUrl) {
        this.mapper = mapper;
        restClient = builder.baseUrl(baseUrl)
                            .defaultHeaders(httpHeaders -> httpHeaders.set(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                            .build();
    }

    @Override
    public List<DomainJoke> getJokes() {
        return getJokeList();
    }

    private List<DomainJoke> getJokeList() {
        List<ExternalJokeApiClientResponse> jokes = new ArrayList<>();
        for (int i = DEFAULT_TOTAL_AMOUNT_TO_RETRIEVE; i > 0; i = i - MAX_AMOUNT_JOKES_PER_REQUEST) {
            jokes.add(
                    Objects.requireNonNull(
                            getJokesFromApi(Math.min(i, MAX_AMOUNT_JOKES_PER_REQUEST))
                    )
            );
        }
        return jokes.stream()
                    .flatMap(externalJokeApiClientResponse -> externalJokeApiClientResponse.jokes().stream())
                    .map(mapper::externalJokeApiClientJokeToDomainJoke)
                    .collect(Collectors.toList());
    }

    private ExternalJokeApiClientResponse getJokesFromApi(int amount) {
        return restClient.get()
                         .uri(
                                 UriComponentsBuilder.newInstance()
                                                     .path(GET_JOKES_ANY_PATH)
                                                     .query(QUERY_PARAMETER_TYPE)
                                                     .query(QUERY_PARAMETER_AMOUNT)
                                                     .buildAndExpand(DEFAULT_TYPE_TO_RETRIEVE, amount)
                                                     .toUriString()
                         )
                         .retrieve()
                         .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(), (request, response) -> {
                             logger.error(
                                     LOG_ERROR_MESSAGE,
                                     request.getURI(),
                                     response.getStatusCode().value(),
                                     response.getStatusText()
                             );
                             throw new ExternalJokeApiClientException(
                                     String.format(
                                             ERROR_WHILE_CALLING_EXTERNAL_JOKE_API_STATUS_S_AND_MESSAGE_S,
                                             response.getStatusCode().value(),
                                             response.getStatusText()
                                     )
                             );
                         })
                         .body(ExternalJokeApiClientResponse.class);
    }
}
