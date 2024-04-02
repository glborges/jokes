package com.example.jokes.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DomainJoke(int id, String type, String joke, Map<String, Boolean> flags, boolean safe) {
}
