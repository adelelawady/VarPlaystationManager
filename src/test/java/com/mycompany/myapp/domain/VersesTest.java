package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VersesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Verses.class);
        Verses verses1 = new Verses();
        verses1.setId("id1");
        Verses verses2 = new Verses();
        verses2.setId(verses1.getId());
        assertThat(verses1).isEqualTo(verses2);
        verses2.setId("id2");
        assertThat(verses1).isNotEqualTo(verses2);
        verses1.setId(null);
        assertThat(verses1).isNotEqualTo(verses2);
    }
}
