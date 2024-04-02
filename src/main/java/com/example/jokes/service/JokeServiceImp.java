package com.example.jokes.service;

import com.example.jokes.adapter.joke.ExternalJokeAdapter;
import com.example.jokes.exception.NoJokesFoundException;
import com.example.jokes.model.DomainJoke;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.function.Predicate;

@Service
public class JokeServiceImp implements JokeService {

    public static final String RACIST_FLAG_KEY = "racist";
    public static final String SEXIST_FLAG_KEY = "sexist";
    public static final String EXPLICIT_FLAG_KEY = "explicit";
    private final ExternalJokeAdapter externalJokeAdapter;

    public JokeServiceImp(ExternalJokeAdapter externalJokeAdapter) {
        this.externalJokeAdapter = externalJokeAdapter;
    }

    @Override
    public DomainJoke getJoke() {
        return externalJokeAdapter.getJokes()
                                  .stream()
                                  .filter(notAcceptableJokesFilter())
                                  .min(Comparator.comparing(domainJoke -> domainJoke.joke().length()))
                                  .orElseThrow(() -> new NoJokesFoundException("There were no acceptable Jokes obtained through the API."));

    }

    private Predicate<DomainJoke> notAcceptableJokesFilter() {
        return domainJoke ->
                domainJoke.safe() &&
                        !domainJoke.flags().getOrDefault(RACIST_FLAG_KEY, true) &&
                        !domainJoke.flags().getOrDefault(SEXIST_FLAG_KEY, true) &&
                        !domainJoke.flags().getOrDefault(EXPLICIT_FLAG_KEY, true);
    }


}
