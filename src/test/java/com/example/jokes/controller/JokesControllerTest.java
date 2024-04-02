package com.example.jokes.controller;

import com.example.jokes.exception.ExternalJokeApiClientException;
import com.example.jokes.exception.NoJokesFoundException;
import com.example.jokes.mapper.DomainJokeJokeDTOMapper;
import com.example.jokes.model.DTOJoke;
import com.example.jokes.model.DomainJoke;
import com.example.jokes.service.JokeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import static com.example.jokes.helper.TestHelper.createRandomDomainJoke;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class JokesControllerTest {

    @Mock
    private JokeService service;
    @Spy
    private DomainJokeJokeDTOMapper mapper = Mappers.getMapper(DomainJokeJokeDTOMapper.class);
    @InjectMocks
    private JokesController controller;

    @Test
    void whenGetJoke_returnValidJoke() {
        DomainJoke expected = createRandomDomainJoke();
        when(service.getJoke()).thenReturn(expected);

        DTOJoke actual = controller.getJoke();

        assertAll(
                () -> assertEquals(expected.id(), actual.id()),
                () -> assertEquals(expected.joke(), actual.randomJoke())
        );
        verify(mapper, times(1)).DomainJokeToDTOJoke(expected);
    }

    @Test
    void givenNoJokesFoundException_whenGetJoke_return404() {
        when(service.getJoke()).thenThrow(new NoJokesFoundException("Not found"));

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> controller.getJoke());

        assertEquals(HttpStatusCode.valueOf(404), responseStatusException.getStatusCode());
        verifyNoInteractions(mapper);
    }

    @Test
    void givenExternalJokeApiClientException_whenGetJokes_return400() {
        when(service.getJoke()).thenThrow(new ExternalJokeApiClientException("Problem with Api."));

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> controller.getJoke());

        assertEquals(HttpStatusCode.valueOf(400), responseStatusException.getStatusCode());
        verifyNoInteractions(mapper);
    }
}