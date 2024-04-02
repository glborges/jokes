package com.example.jokes.mapper;

import com.example.jokes.model.DTOJoke;
import com.example.jokes.model.DomainJoke;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.example.jokes.helper.TestHelper.createRandomDomainJoke;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class DomainJokeJokeDTOMapperTest {

    @Spy
    private DomainJokeJokeDTOMapper mapper = Mappers.getMapper(DomainJokeJokeDTOMapper.class);

    @Test
    void domainJokeToDTOJoke() {
        DomainJoke expected = createRandomDomainJoke();

        DTOJoke actual = mapper.DomainJokeToDTOJoke(expected);

        assertAll(
                () -> assertEquals(expected.id(), actual.id()),
                () -> assertEquals(expected.joke(), actual.randomJoke())
        );
    }
}