package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SessionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionDTO.class);
        SessionDTO sessionDTO1 = new SessionDTO();
        sessionDTO1.setId("id1");
        SessionDTO sessionDTO2 = new SessionDTO();
        assertThat(sessionDTO1).isNotEqualTo(sessionDTO2);
        sessionDTO2.setId(sessionDTO1.getId());
        assertThat(sessionDTO1).isEqualTo(sessionDTO2);
        sessionDTO2.setId("id2");
        assertThat(sessionDTO1).isNotEqualTo(sessionDTO2);
        sessionDTO1.setId(null);
        assertThat(sessionDTO1).isNotEqualTo(sessionDTO2);
    }
}
