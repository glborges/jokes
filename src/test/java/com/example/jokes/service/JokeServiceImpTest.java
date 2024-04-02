package com.example.jokes.service;

import com.example.jokes.adapter.joke.ExternalJokeAdapter;
import com.example.jokes.exception.NoJokesFoundException;
import com.example.jokes.model.DomainJoke;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.example.jokes.helper.TestHelper.createDomainJokeWithFlags;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class JokeServiceImpTest {

    @Mock
    private ExternalJokeAdapter adapter;
    @InjectMocks
    private JokeServiceImp service;

    @Test
    void givenListOfDomainJokes_whenGetJokes_returnSmallestFilteredJoke() {
        int expectedId = 3;
        String expectedJokeText = "123";
        when(adapter.getJokes()).thenReturn(
                List.of(
                        createDomainJokeWithFlags(1, "1", false, false, false, false),
                        createDomainJokeWithFlags(2, "12", false, true, false, true),
                        createDomainJokeWithFlags(expectedId, expectedJokeText, false, false, false, true),
                        createDomainJokeWithFlags(4, "12345", false, false, false, true),
                        createDomainJokeWithFlags(5, "123456", false, false, false, true),
                        createDomainJokeWithFlags(6, "1234567", false, false, false, true),
                        createDomainJokeWithFlags(7, "123", true, false, false, true),
                        createDomainJokeWithFlags(8, "12", true, false, false, true)
                )
        );

        DomainJoke actual = service.getJoke();

        assertAll(
                () -> assertEquals(expectedId, actual.id()),
                () -> assertEquals(expectedJokeText, actual.joke())
        );
    }

    @Test
    void givenListOfNotAcceptableDomainJokes_whenGetJokes_returnException() {
        when(adapter.getJokes()).thenReturn(
                List.of(
                        createDomainJokeWithFlags(1, "1", false, false, false, false),
                        createDomainJokeWithFlags(2, "12", true, false, false, true),
                        createDomainJokeWithFlags(3, "123", false, true, false, true),
                        createDomainJokeWithFlags(4, "12345", false, false, true, true),
                        createDomainJokeWithFlags(5, "123456", true, true, false, true),
                        createDomainJokeWithFlags(6, "1234567", false, true, true, true),
                        createDomainJokeWithFlags(7, "123", true, false, true, true)
                )
        );

        assertThrows(NoJokesFoundException.class, () -> service.getJoke());
    }

    @Test
    void givenListOfDomainJokes_whenGetJokesFindsTwoWithSameSize_returnTheFirstOne() {
        int expectedId = 3;
        String expectedJokeText = "12345";
        when(adapter.getJokes()).thenReturn(
                List.of(
                        createDomainJokeWithFlags(1, "1", false, false, false, false),
                        createDomainJokeWithFlags(2, "12", false, true, false, true),
                        createDomainJokeWithFlags(3, "12345", false, false, false, true),
                        createDomainJokeWithFlags(4, "12345", false, false, false, true),
                        createDomainJokeWithFlags(5, "123456", false, false, false, true),
                        createDomainJokeWithFlags(6, "1234567", false, false, false, true),
                        createDomainJokeWithFlags(7, "123", true, false, false, true),
                        createDomainJokeWithFlags(8, "12", true, false, false, true)
                )
        );

        DomainJoke actual = service.getJoke();

        assertAll(
                () -> assertEquals(expectedId, actual.id()),
                () -> assertEquals(expectedJokeText, actual.joke())
        );
    }
}