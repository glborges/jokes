package com.example.jokes.mapper;

import com.example.jokes.model.DTOJoke;
import com.example.jokes.model.DomainJoke;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DomainJokeJokeDTOMapper {
    @Mapping(target = "randomJoke", source = "domainJoke.joke")
    DTOJoke DomainJokeToDTOJoke(DomainJoke domainJoke);
}
