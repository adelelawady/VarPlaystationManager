package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Table;
import com.mycompany.myapp.repository.TableRepository;
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
 * Integration tests for the {@link TableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TableResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private MockMvc restTableMockMvc;

    private Table table;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Table createEntity() {
        Table table = new Table().name(DEFAULT_NAME).discount(DEFAULT_DISCOUNT).totalPrice(DEFAULT_TOTAL_PRICE);
        return table;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Table createUpdatedEntity() {
        Table table = new Table().name(UPDATED_NAME).discount(UPDATED_DISCOUNT).totalPrice(UPDATED_TOTAL_PRICE);
        return table;
    }

    @BeforeEach
    public void initTest() {
        tableRepository.deleteAll();
        table = createEntity();
    }

    @Test
    void createTable() throws Exception {
        int databaseSizeBeforeCreate = tableRepository.findAll().size();
        // Create the Table
        restTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(table)))
            .andExpect(status().isCreated());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeCreate + 1);
        Table testTable = tableList.get(tableList.size() - 1);
        assertThat(testTable.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTable.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testTable.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
    }

    @Test
    void createTableWithExistingId() throws Exception {
        // Create the Table with an existing ID
        table.setId("existing_id");

        int databaseSizeBeforeCreate = tableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(table)))
            .andExpect(status().isBadRequest());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllTables() throws Exception {
        // Initialize the database
        table.setId(UUID.randomUUID().toString());
        tableRepository.save(table);

        // Get all the tableList
        restTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(table.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())));
    }

    @Test
    void getTable() throws Exception {
        // Initialize the database
        table.setId(UUID.randomUUID().toString());
        tableRepository.save(table);

        // Get the table
        restTableMockMvc
            .perform(get(ENTITY_API_URL_ID, table.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(table.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()));
    }

    @Test
    void getNonExistingTable() throws Exception {
        // Get the table
        restTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewTable() throws Exception {
        // Initialize the database
        table.setId(UUID.randomUUID().toString());
        tableRepository.save(table);

        int databaseSizeBeforeUpdate = tableRepository.findAll().size();

        // Update the table
        Table updatedTable = tableRepository.findById(table.getId()).get();
        updatedTable.name(UPDATED_NAME).discount(UPDATED_DISCOUNT).totalPrice(UPDATED_TOTAL_PRICE);

        restTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTable))
            )
            .andExpect(status().isOk());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeUpdate);
        Table testTable = tableList.get(tableList.size() - 1);
        assertThat(testTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTable.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testTable.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    void putNonExistingTable() throws Exception {
        int databaseSizeBeforeUpdate = tableRepository.findAll().size();
        table.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, table.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(table))
            )
            .andExpect(status().isBadRequest());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTable() throws Exception {
        int databaseSizeBeforeUpdate = tableRepository.findAll().size();
        table.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(table))
            )
            .andExpect(status().isBadRequest());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTable() throws Exception {
        int databaseSizeBeforeUpdate = tableRepository.findAll().size();
        table.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(table)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTableWithPatch() throws Exception {
        // Initialize the database
        table.setId(UUID.randomUUID().toString());
        tableRepository.save(table);

        int databaseSizeBeforeUpdate = tableRepository.findAll().size();

        // Update the table using partial update
        Table partialUpdatedTable = new Table();
        partialUpdatedTable.setId(table.getId());

        partialUpdatedTable.totalPrice(UPDATED_TOTAL_PRICE);

        restTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTable))
            )
            .andExpect(status().isOk());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeUpdate);
        Table testTable = tableList.get(tableList.size() - 1);
        assertThat(testTable.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTable.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testTable.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    void fullUpdateTableWithPatch() throws Exception {
        // Initialize the database
        table.setId(UUID.randomUUID().toString());
        tableRepository.save(table);

        int databaseSizeBeforeUpdate = tableRepository.findAll().size();

        // Update the table using partial update
        Table partialUpdatedTable = new Table();
        partialUpdatedTable.setId(table.getId());

        partialUpdatedTable.name(UPDATED_NAME).discount(UPDATED_DISCOUNT).totalPrice(UPDATED_TOTAL_PRICE);

        restTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTable))
            )
            .andExpect(status().isOk());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeUpdate);
        Table testTable = tableList.get(tableList.size() - 1);
        assertThat(testTable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTable.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testTable.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    void patchNonExistingTable() throws Exception {
        int databaseSizeBeforeUpdate = tableRepository.findAll().size();
        table.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, table.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(table))
            )
            .andExpect(status().isBadRequest());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTable() throws Exception {
        int databaseSizeBeforeUpdate = tableRepository.findAll().size();
        table.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(table))
            )
            .andExpect(status().isBadRequest());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTable() throws Exception {
        int databaseSizeBeforeUpdate = tableRepository.findAll().size();
        table.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(table)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Table in the database
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTable() throws Exception {
        // Initialize the database
        table.setId(UUID.randomUUID().toString());
        tableRepository.save(table);

        int databaseSizeBeforeDelete = tableRepository.findAll().size();

        // Delete the table
        restTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, table.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Table> tableList = tableRepository.findAll();
        assertThat(tableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
