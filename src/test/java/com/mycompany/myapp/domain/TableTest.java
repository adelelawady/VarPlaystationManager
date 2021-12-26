package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Table.class);
        Table table1 = new Table();
        table1.setId("id1");
        Table table2 = new Table();
        table2.setId(table1.getId());
        assertThat(table1).isEqualTo(table2);
        table2.setId("id2");
        assertThat(table1).isNotEqualTo(table2);
        table1.setId(null);
        assertThat(table1).isNotEqualTo(table2);
    }
}
