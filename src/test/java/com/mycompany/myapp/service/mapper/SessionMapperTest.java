package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SessionMapperTest {

    private SessionMapper sessionMapper;

    @BeforeEach
    public void setUp() {
        sessionMapper = new SessionMapperImpl();
    }
}
