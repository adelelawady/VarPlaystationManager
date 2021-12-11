package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Device;
import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.repository.RecordRepository;
import com.mycompany.myapp.service.RecordService;
import com.mycompany.myapp.service.dto.RecordDTO;
import com.mycompany.myapp.service.mapper.RecordMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link RecordResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RecordResourceIT {

    private static final Instant DEFAULT_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RecordRepository recordRepository;

    @Mock
    private RecordRepository recordRepositoryMock;

    @Autowired
    private RecordMapper recordMapper;

    @Mock
    private RecordService recordServiceMock;

    @Autowired
    private MockMvc restRecordMockMvc;

    private Record record;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Record createEntity() {
        Record record = new Record().start(DEFAULT_START).end(DEFAULT_END).totalPrice(DEFAULT_TOTAL_PRICE);
        // Add required entity
        Device device;
        device = DeviceResourceIT.createEntity();
        device.setId("fixed-id-for-tests");
        record.setDevice(device);
        return record;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Record createUpdatedEntity() {
        Record record = new Record().start(UPDATED_START).end(UPDATED_END).totalPrice(UPDATED_TOTAL_PRICE);
        // Add required entity
        Device device;
        device = DeviceResourceIT.createUpdatedEntity();
        device.setId("fixed-id-for-tests");
        record.setDevice(device);
        return record;
    }

    @BeforeEach
    public void initTest() {
        recordRepository.deleteAll();
        record = createEntity();
    }

    @Test
    void createRecord() throws Exception {
        int databaseSizeBeforeCreate = recordRepository.findAll().size();
        // Create the Record
        RecordDTO recordDTO = recordMapper.toDto(record);
        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
            .andExpect(status().isCreated());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeCreate + 1);
        Record testRecord = recordList.get(recordList.size() - 1);
        assertThat(testRecord.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testRecord.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testRecord.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
    }

    @Test
    void createRecordWithExistingId() throws Exception {
        // Create the Record with an existing ID
        record.setId("existing_id");
        RecordDTO recordDTO = recordMapper.toDto(record);

        int databaseSizeBeforeCreate = recordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRecords() throws Exception {
        // Initialize the database
        record.setId(UUID.randomUUID().toString());
        recordRepository.save(record);

        // Get all the recordList
        restRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(record.getId())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecordsWithEagerRelationshipsIsEnabled() throws Exception {
        when(recordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recordServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecordsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(recordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recordServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getRecord() throws Exception {
        // Initialize the database
        record.setId(UUID.randomUUID().toString());
        recordRepository.save(record);

        // Get the record
        restRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, record.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(record.getId()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START.toString()))
            .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()));
    }

    @Test
    void getNonExistingRecord() throws Exception {
        // Get the record
        restRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewRecord() throws Exception {
        // Initialize the database
        record.setId(UUID.randomUUID().toString());
        recordRepository.save(record);

        int databaseSizeBeforeUpdate = recordRepository.findAll().size();

        // Update the record
        Record updatedRecord = recordRepository.findById(record.getId()).get();
        updatedRecord.start(UPDATED_START).end(UPDATED_END).totalPrice(UPDATED_TOTAL_PRICE);
        RecordDTO recordDTO = recordMapper.toDto(updatedRecord);

        restRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recordDTO))
            )
            .andExpect(status().isOk());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
        Record testRecord = recordList.get(recordList.size() - 1);
        assertThat(testRecord.getStart()).isEqualTo(UPDATED_START);
        assertThat(testRecord.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testRecord.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    void putNonExistingRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.setId(UUID.randomUUID().toString());

        // Create the Record
        RecordDTO recordDTO = recordMapper.toDto(record);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.setId(UUID.randomUUID().toString());

        // Create the Record
        RecordDTO recordDTO = recordMapper.toDto(record);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.setId(UUID.randomUUID().toString());

        // Create the Record
        RecordDTO recordDTO = recordMapper.toDto(record);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRecordWithPatch() throws Exception {
        // Initialize the database
        record.setId(UUID.randomUUID().toString());
        recordRepository.save(record);

        int databaseSizeBeforeUpdate = recordRepository.findAll().size();

        // Update the record using partial update
        Record partialUpdatedRecord = new Record();
        partialUpdatedRecord.setId(record.getId());

        restRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecord))
            )
            .andExpect(status().isOk());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
        Record testRecord = recordList.get(recordList.size() - 1);
        assertThat(testRecord.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testRecord.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testRecord.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
    }

    @Test
    void fullUpdateRecordWithPatch() throws Exception {
        // Initialize the database
        record.setId(UUID.randomUUID().toString());
        recordRepository.save(record);

        int databaseSizeBeforeUpdate = recordRepository.findAll().size();

        // Update the record using partial update
        Record partialUpdatedRecord = new Record();
        partialUpdatedRecord.setId(record.getId());

        partialUpdatedRecord.start(UPDATED_START).end(UPDATED_END).totalPrice(UPDATED_TOTAL_PRICE);

        restRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecord))
            )
            .andExpect(status().isOk());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
        Record testRecord = recordList.get(recordList.size() - 1);
        assertThat(testRecord.getStart()).isEqualTo(UPDATED_START);
        assertThat(testRecord.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testRecord.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    void patchNonExistingRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.setId(UUID.randomUUID().toString());

        // Create the Record
        RecordDTO recordDTO = recordMapper.toDto(record);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.setId(UUID.randomUUID().toString());

        // Create the Record
        RecordDTO recordDTO = recordMapper.toDto(record);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.setId(UUID.randomUUID().toString());

        // Create the Record
        RecordDTO recordDTO = recordMapper.toDto(record);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(recordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRecord() throws Exception {
        // Initialize the database
        record.setId(UUID.randomUUID().toString());
        recordRepository.save(record);

        int databaseSizeBeforeDelete = recordRepository.findAll().size();

        // Delete the record
        restRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, record.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
