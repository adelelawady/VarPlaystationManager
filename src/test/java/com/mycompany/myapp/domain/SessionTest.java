package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Session.class);
        Session session1 = new Session();
        session1.setId("id1");
        Session session2 = new Session();
        session2.setId(session1.getId());
        assertThat(session1).isEqualTo(session2);
        session2.setId("id2");
        assertThat(session1).isNotEqualTo(session2);
        session1.setId(null);
        assertThat(session1).isNotEqualTo(session2);
    }
}
