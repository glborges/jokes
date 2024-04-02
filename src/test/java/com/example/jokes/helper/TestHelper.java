package com.example.jokes.helper;

import com.example.jokes.adapter.joke.model.ExternalJokeApiClientJoke;
import com.example.jokes.adapter.joke.model.ExternalJokeApiClientResponse;
import com.example.jokes.model.DomainJoke;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.jokes.service.JokeServiceImp.*;

public class TestHelper {

    public static final String EXTERNAL_JOKE_API_HOST = "v2.banana.dev";
    public static final String EXTERNAL_JOKE_API_SCHEME = "https";

    public static ExternalJokeApiClientResponse createExternalJokeApiClientResponseWithAmountJokes(int amount) {
        return new ExternalJokeApiClientResponse("", amount, createListExternalJokeApiClientJoke(amount));
    }

    public static List<ExternalJokeApiClientJoke> createListExternalJokeApiClientJoke(int amount) {
        return Stream.generate(TestHelper::createRandomExternalJokeApiClientJoke)
                     .limit(amount)
                     .collect(Collectors.toList());
    }

    public static ExternalJokeApiClientJoke createRandomExternalJokeApiClientJoke() {
        int randomNumber = ((int) (Math.random() * 1000));
        return new ExternalJokeApiClientJoke(
                randomNumber,
                "single",
                "Banana" + randomNumber,
                Map.of(
                        "test1", randomNumber % 2 == 0,
                        "test2", randomNumber % 3 == 0
                ),
                randomNumber % 2 == 0
        );
    }

    public static DomainJoke createRandomDomainJoke() {
        int randomNumber = ((int) (Math.random() * 1000));
        return new DomainJoke(
                randomNumber,
                "Single",
                "Banana" + randomNumber,
                Map.of(
                        "test1", randomNumber % 2 == 0,
                        "test2", randomNumber % 3 == 0
                ),
                randomNumber % 2 == 0
        );
    }

    public static DomainJoke createDomainJokeWithFlags(int id, String joke, boolean sexist, boolean racist, boolean explicit, boolean safe) {
        return new DomainJoke(
                id,
                "Single",
                joke,
                Map.of(
                        SEXIST_FLAG_KEY, sexist,
                        RACIST_FLAG_KEY, racist,
                        EXPLICIT_FLAG_KEY, explicit
                ),
                safe
        );
    }

    public static ExternalJokeApiClientJoke createExternalJokeApiClientJoke(int id, String joke, boolean sexist, boolean racist, boolean explicit, boolean safe) {
        return new ExternalJokeApiClientJoke(
                id,
                "Single",
                joke,
                Map.of(
                        SEXIST_FLAG_KEY, sexist,
                        RACIST_FLAG_KEY, racist,
                        EXPLICIT_FLAG_KEY, explicit
                ),
                safe
        );
    }
}
