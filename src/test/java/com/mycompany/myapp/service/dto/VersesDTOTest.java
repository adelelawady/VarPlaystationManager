package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VersesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VersesDTO.class);
        VersesDTO versesDTO1 = new VersesDTO();
        versesDTO1.setId("id1");
        VersesDTO versesDTO2 = new VersesDTO();
        assertThat(versesDTO1).isNotEqualTo(versesDTO2);
        versesDTO2.setId(versesDTO1.getId());
        assertThat(versesDTO1).isEqualTo(versesDTO2);
        versesDTO2.setId("id2");
        assertThat(versesDTO1).isNotEqualTo(versesDTO2);
        versesDTO1.setId(null);
        assertThat(versesDTO1).isNotEqualTo(versesDTO2);
    }
}
