package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VerseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Verse.class);
        Verse verse1 = new Verse();
        verse1.setId("id1");
        Verse verse2 = new Verse();
        verse2.setId(verse1.getId());
        assertThat(verse1).isEqualTo(verse2);
        verse2.setId("id2");
        assertThat(verse1).isNotEqualTo(verse2);
        verse1.setId(null);
        assertThat(verse1).isNotEqualTo(verse2);
    }
}
