package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TakeawayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Takeaway.class);
        Takeaway takeaway1 = new Takeaway();
        takeaway1.setId("id1");
        Takeaway takeaway2 = new Takeaway();
        takeaway2.setId(takeaway1.getId());
        assertThat(takeaway1).isEqualTo(takeaway2);
        takeaway2.setId("id2");
        assertThat(takeaway1).isNotEqualTo(takeaway2);
        takeaway1.setId(null);
        assertThat(takeaway1).isNotEqualTo(takeaway2);
    }
}
