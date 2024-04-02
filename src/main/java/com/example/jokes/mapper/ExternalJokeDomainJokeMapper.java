package com.example.jokes.mapper;

import com.example.jokes.adapter.joke.model.ExternalJokeApiClientJoke;
import com.example.jokes.model.DomainJoke;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExternalJokeDomainJokeMapper {
    DomainJoke externalJokeApiClientJokeToDomainJoke(ExternalJokeApiClientJoke externalJokeApiClientJoke);
}
