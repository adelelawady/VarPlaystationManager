package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.DeviceType;
import com.mycompany.myapp.repository.DeviceTypeRepository;
import com.mycompany.myapp.service.dto.DeviceTypeDTO;
import com.mycompany.myapp.service.mapper.DeviceTypeMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link DeviceTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE_PER_HOUR = 1D;
    private static final Double UPDATED_PRICE_PER_HOUR = 2D;

    private static final String ENTITY_API_URL = "/api/device-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DeviceTypeRepository deviceTypeRepository;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Autowired
    private MockMvc restDeviceTypeMockMvc;

    private DeviceType deviceType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceType createEntity() {
        DeviceType deviceType = new DeviceType().name(DEFAULT_NAME).pricePerHour(DEFAULT_PRICE_PER_HOUR);
        return deviceType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceType createUpdatedEntity() {
        DeviceType deviceType = new DeviceType().name(UPDATED_NAME).pricePerHour(UPDATED_PRICE_PER_HOUR);
        return deviceType;
    }

    @BeforeEach
    public void initTest() {
        deviceTypeRepository.deleteAll();
        deviceType = createEntity();
    }

    @Test
    void createDeviceType() throws Exception {
        int databaseSizeBeforeCreate = deviceTypeRepository.findAll().size();
        // Create the DeviceType
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);
        restDeviceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeviceType.getPricePerHour()).isEqualTo(DEFAULT_PRICE_PER_HOUR);
    }

    @Test
    void createDeviceTypeWithExistingId() throws Exception {
        // Create the DeviceType with an existing ID
        deviceType.setId("existing_id");
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);

        int databaseSizeBeforeCreate = deviceTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDeviceTypes() throws Exception {
        // Initialize the database
        deviceType.setId(UUID.randomUUID().toString());
        deviceTypeRepository.save(deviceType);

        // Get all the deviceTypeList
        restDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceType.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pricePerHour").value(hasItem(DEFAULT_PRICE_PER_HOUR.doubleValue())));
    }

    @Test
    void getDeviceType() throws Exception {
        // Initialize the database
        deviceType.setId(UUID.randomUUID().toString());
        deviceTypeRepository.save(deviceType);

        // Get the deviceType
        restDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, deviceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deviceType.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.pricePerHour").value(DEFAULT_PRICE_PER_HOUR.doubleValue()));
    }

    @Test
    void getNonExistingDeviceType() throws Exception {
        // Get the deviceType
        restDeviceTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewDeviceType() throws Exception {
        // Initialize the database
        deviceType.setId(UUID.randomUUID().toString());
        deviceTypeRepository.save(deviceType);

        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();

        // Update the deviceType
        DeviceType updatedDeviceType = deviceTypeRepository.findById(deviceType.getId()).get();
        updatedDeviceType.name(UPDATED_NAME).pricePerHour(UPDATED_PRICE_PER_HOUR);
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(updatedDeviceType);

        restDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeviceType.getPricePerHour()).isEqualTo(UPDATED_PRICE_PER_HOUR);
    }

    @Test
    void putNonExistingDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(UUID.randomUUID().toString());

        // Create the DeviceType
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(UUID.randomUUID().toString());

        // Create the DeviceType
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(UUID.randomUUID().toString());

        // Create the DeviceType
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDeviceTypeWithPatch() throws Exception {
        // Initialize the database
        deviceType.setId(UUID.randomUUID().toString());
        deviceTypeRepository.save(deviceType);

        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();

        // Update the deviceType using partial update
        DeviceType partialUpdatedDeviceType = new DeviceType();
        partialUpdatedDeviceType.setId(deviceType.getId());

        partialUpdatedDeviceType.name(UPDATED_NAME).pricePerHour(UPDATED_PRICE_PER_HOUR);

        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceType))
            )
            .andExpect(status().isOk());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeviceType.getPricePerHour()).isEqualTo(UPDATED_PRICE_PER_HOUR);
    }

    @Test
    void fullUpdateDeviceTypeWithPatch() throws Exception {
        // Initialize the database
        deviceType.setId(UUID.randomUUID().toString());
        deviceTypeRepository.save(deviceType);

        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();

        // Update the deviceType using partial update
        DeviceType partialUpdatedDeviceType = new DeviceType();
        partialUpdatedDeviceType.setId(deviceType.getId());

        partialUpdatedDeviceType.name(UPDATED_NAME).pricePerHour(UPDATED_PRICE_PER_HOUR);

        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceType))
            )
            .andExpect(status().isOk());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
        DeviceType testDeviceType = deviceTypeList.get(deviceTypeList.size() - 1);
        assertThat(testDeviceType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeviceType.getPricePerHour()).isEqualTo(UPDATED_PRICE_PER_HOUR);
    }

    @Test
    void patchNonExistingDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(UUID.randomUUID().toString());

        // Create the DeviceType
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(UUID.randomUUID().toString());

        // Create the DeviceType
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = deviceTypeRepository.findAll().size();
        deviceType.setId(UUID.randomUUID().toString());

        // Create the DeviceType
        DeviceTypeDTO deviceTypeDTO = deviceTypeMapper.toDto(deviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deviceTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceType in the database
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDeviceType() throws Exception {
        // Initialize the database
        deviceType.setId(UUID.randomUUID().toString());
        deviceTypeRepository.save(deviceType);

        int databaseSizeBeforeDelete = deviceTypeRepository.findAll().size();

        // Delete the deviceType
        restDeviceTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, deviceType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeviceType> deviceTypeList = deviceTypeRepository.findAll();
        assertThat(deviceTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
