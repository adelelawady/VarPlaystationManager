package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeviceTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceType.class);
        DeviceType deviceType1 = new DeviceType();
        deviceType1.setId("id1");
        DeviceType deviceType2 = new DeviceType();
        deviceType2.setId(deviceType1.getId());
        assertThat(deviceType1).isEqualTo(deviceType2);
        deviceType2.setId("id2");
        assertThat(deviceType1).isNotEqualTo(deviceType2);
        deviceType1.setId(null);
        assertThat(deviceType1).isNotEqualTo(deviceType2);
    }
}
