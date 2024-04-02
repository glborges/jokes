package com.example.jokes.controller;

import com.example.jokes.exception.ExternalJokeApiClientException;
import com.example.jokes.exception.NoJokesFoundException;
import com.example.jokes.mapper.DomainJokeJokeDTOMapper;
import com.example.jokes.model.DTOJoke;
import com.example.jokes.service.JokeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@ResponseBody
public class JokesController {

    private final JokeService service;
    private final DomainJokeJokeDTOMapper mapper;

    public JokesController(JokeService service, DomainJokeJokeDTOMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/v1/joke")
    public DTOJoke getJoke() {
        try {
            return mapper.DomainJokeToDTOJoke(service.getJoke());
        } catch (ExternalJokeApiClientException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, exception.getLocalizedMessage()
            );
        } catch (NoJokesFoundException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, exception.getLocalizedMessage()
            );
        }
    }
}
