package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecordDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecordDTO.class);
        RecordDTO recordDTO1 = new RecordDTO();
        recordDTO1.setId("id1");
        RecordDTO recordDTO2 = new RecordDTO();
        assertThat(recordDTO1).isNotEqualTo(recordDTO2);
        recordDTO2.setId(recordDTO1.getId());
        assertThat(recordDTO1).isEqualTo(recordDTO2);
        recordDTO2.setId("id2");
        assertThat(recordDTO1).isNotEqualTo(recordDTO2);
        recordDTO1.setId(null);
        assertThat(recordDTO1).isNotEqualTo(recordDTO2);
    }
}
