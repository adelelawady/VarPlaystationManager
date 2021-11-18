package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VerseMapperTest {

    private VerseMapper verseMapper;

    @BeforeEach
    public void setUp() {
        verseMapper = new VerseMapperImpl();
    }
}
