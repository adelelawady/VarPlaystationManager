package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeviceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceDTO.class);
        DeviceDTO deviceDTO1 = new DeviceDTO();
        deviceDTO1.setId("id1");
        DeviceDTO deviceDTO2 = new DeviceDTO();
        assertThat(deviceDTO1).isNotEqualTo(deviceDTO2);
        deviceDTO2.setId(deviceDTO1.getId());
        assertThat(deviceDTO1).isEqualTo(deviceDTO2);
        deviceDTO2.setId("id2");
        assertThat(deviceDTO1).isNotEqualTo(deviceDTO2);
        deviceDTO1.setId(null);
        assertThat(deviceDTO1).isNotEqualTo(deviceDTO2);
    }
}
