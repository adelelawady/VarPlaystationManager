package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Record.class);
        Record record1 = new Record();
        record1.setId("id1");
        Record record2 = new Record();
        record2.setId(record1.getId());
        assertThat(record1).isEqualTo(record2);
        record2.setId("id2");
        assertThat(record1).isNotEqualTo(record2);
        record1.setId(null);
        assertThat(record1).isNotEqualTo(record2);
    }
}
