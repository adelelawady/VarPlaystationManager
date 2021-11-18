package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VersesMapperTest {

    private VersesMapper versesMapper;

    @BeforeEach
    public void setUp() {
        versesMapper = new VersesMapperImpl();
    }
}
