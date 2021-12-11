package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecordMapperTest {

    private RecordMapper recordMapper;

    @BeforeEach
    public void setUp() {
        recordMapper = new RecordMapperImpl();
    }
}
