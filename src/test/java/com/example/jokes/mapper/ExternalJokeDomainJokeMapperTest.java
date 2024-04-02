package com.example.jokes.mapper;

import com.example.jokes.adapter.joke.model.ExternalJokeApiClientJoke;
import com.example.jokes.model.DomainJoke;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class ExternalJokeDomainJokeMapperTest {

    @Spy
    private ExternalJokeDomainJokeMapper mapper = Mappers.getMapper(ExternalJokeDomainJokeMapper.class);

    @Test
    void externalJokeApiClientJokeToDomainJoke() {
        ExternalJokeApiClientJoke expected = new ExternalJokeApiClientJoke(
                1,
                "single",
                "Banana",
                Map.of(
                        "test1", true,
                        "test2", false
                ),
                true
        );

        DomainJoke actual = mapper.externalJokeApiClientJokeToDomainJoke(expected);

        assertAll(
                () -> assertEquals(expected.id(), actual.id()),
                () -> assertEquals(expected.joke(), actual.joke()),
                () -> assertEquals(expected.type(), actual.type()),
                () -> assertEquals(expected.safe(), actual.safe()),
                () -> assertEquals(expected.flags(), actual.flags())
        );
    }
}