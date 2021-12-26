package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShopsOrdersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopsOrders.class);
        ShopsOrders shopsOrders1 = new ShopsOrders();
        shopsOrders1.setId("id1");
        ShopsOrders shopsOrders2 = new ShopsOrders();
        shopsOrders2.setId(shopsOrders1.getId());
        assertThat(shopsOrders1).isEqualTo(shopsOrders2);
        shopsOrders2.setId("id2");
        assertThat(shopsOrders1).isNotEqualTo(shopsOrders2);
        shopsOrders1.setId(null);
        assertThat(shopsOrders1).isNotEqualTo(shopsOrders2);
    }
}
