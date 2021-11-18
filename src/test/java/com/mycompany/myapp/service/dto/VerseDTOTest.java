package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VerseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VerseDTO.class);
        VerseDTO verseDTO1 = new VerseDTO();
        verseDTO1.setId("id1");
        VerseDTO verseDTO2 = new VerseDTO();
        assertThat(verseDTO1).isNotEqualTo(verseDTO2);
        verseDTO2.setId(verseDTO1.getId());
        assertThat(verseDTO1).isEqualTo(verseDTO2);
        verseDTO2.setId("id2");
        assertThat(verseDTO1).isNotEqualTo(verseDTO2);
        verseDTO1.setId(null);
        assertThat(verseDTO1).isNotEqualTo(verseDTO2);
    }
}
