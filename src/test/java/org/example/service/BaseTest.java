package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class BaseTest {

    @SneakyThrows
    protected static String asJsonString(final Object obj) {
            return new ObjectMapper().writeValueAsString(obj);
    }
}
