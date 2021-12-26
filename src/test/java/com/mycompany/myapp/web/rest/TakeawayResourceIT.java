package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Takeaway;
import com.mycompany.myapp.repository.TakeawayRepository;
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
 * Integration tests for the {@link TakeawayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TakeawayResourceIT {

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/takeaways";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private TakeawayRepository takeawayRepository;

    @Autowired
    private MockMvc restTakeawayMockMvc;

    private Takeaway takeaway;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Takeaway createEntity() {
        Takeaway takeaway = new Takeaway().totalPrice(DEFAULT_TOTAL_PRICE);
        return takeaway;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Takeaway createUpdatedEntity() {
        Takeaway takeaway = new Takeaway().totalPrice(UPDATED_TOTAL_PRICE);
        return takeaway;
    }

    @BeforeEach
    public void initTest() {
        takeawayRepository.deleteAll();
        takeaway = createEntity();
    }

    @Test
    void createTakeaway() throws Exception {
        int databaseSizeBeforeCreate = takeawayRepository.findAll().size();
        // Create the Takeaway
        restTakeawayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(takeaway)))
            .andExpect(status().isCreated());

        // Validate the Takeaway in the database
        List<Takeaway> takeawayList = takeawayRepository.findAll();
        assertThat(takeawayList).hasSize(databaseSizeBeforeCreate + 1);
        Takeaway testTakeaway = takeawayList.get(takeawayList.size() - 1);
        assertThat(testTakeaway.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
    }

    @Test
    void createTakeawayWithExistingId() throws Exception {
        // Create the Takeaway with an existing ID
        takeaway.setId("existing_id");

        int databaseSizeBeforeCreate = takeawayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTakeawayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(takeaway)))
            .andExpect(status().isBadRequest());

        // Validate the Takeaway in the database
        List<Takeaway> takeawayList = takeawayRepository.findAll();
        assertThat(takeawayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllTakeaways() throws Exception {
        // Initialize the database
        takeaway.setId(UUID.randomUUID().toString());
        takeawayRepository.save(takeaway);

        // Get all the takeawayList
        restTakeawayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(takeaway.getId())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())));
    }

    @Test
    void getTakeaway() throws Exception {
        // Initialize the database
        takeaway.setId(UUID.randomUUID().toString());
        takeawayRepository.save(takeaway);

        // Get the takeaway
        restTakeawayMockMvc
            .perform(get(ENTITY_API_URL_ID, takeaway.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(takeaway.getId()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()));
    }

    @Test
    void getNonExistingTakeaway() throws Exception {
        // Get the takeaway
        restTakeawayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewTakeaway() throws Exception {
        // Initialize the database
        takeaway.setId(UUID.randomUUID().toString());
        takeawayRepository.save(takeaway);

        int databaseSizeBeforeUpdate = takeawayRepository.findAll().size();

        // Update the takeaway
        Takeaway updatedTakeaway = takeawayRepository.findById(takeaway.getId()).get();
        updatedTakeaway.totalPrice(UPDATED_TOTAL_PRICE);

        restTakeawayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTakeaway.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTakeaway))
            )
            .andExpect(status().isOk());

        // Validate the Takeaway in the database
        List<Takeaway> takeawayList = takeawayRepository.findAll();
        assertThat(takeawayList).hasSize(databaseSizeBeforeUpdate);
        Takeaway testTakeaway = takeawayList.get(takeawayList.size() - 1);
        assertThat(testTakeaway.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    void putNonExistingTakeaway() throws Exception {
        int databaseSizeBeforeUpdate = takeawayRepository.findAll().size();
        takeaway.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTakeawayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, takeaway.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(takeaway))
            )
            .andExpect(status().isBadRequest());

        // Validate the Takeaway in the database
        List<Takeaway> takeawayList = takeawayRepository.findAll();
        assertThat(takeawayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTakeaway() throws Exception {
        int databaseSizeBeforeUpdate = takeawayRepository.findAll().size();
        takeaway.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTakeawayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(takeaway))
            )
            .andExpect(status().isBadRequest());

        // Validate the Takeaway in the database
        List<Takeaway> takeawayList = takeawayRepository.findAll();
        assertThat(takeawayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTakeaway() throws Exception {
        int databaseSizeBeforeUpdate = takeawayRepository.findAll().size();
        takeaway.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTakeawayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(takeaway)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Takeaway in the database
        List<Takeaway> takeawayList = takeawayRepository.findAll();
        assertThat(takeawayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTakeawayWithPatch() throws Exception {
        // Initialize the database
        takeaway.setId(UUID.randomUUID().toString());
        takeawayRepository.save(takeaway);

        int databaseSizeBeforeUpdate = takeawayRepository.findAll().size();

        // Update the takeaway using partial update
        Takeaway partialUpdatedTakeaway = new Takeaway();
        partialUpdatedTakeaway.setId(takeaway.getId());

        restTakeawayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTakeaway.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTakeaway))
            )
            .andExpect(status().isOk());

        // Validate the Takeaway in the database
        List<Takeaway> takeawayList = takeawayRepository.findAll();
        assertThat(takeawayList).hasSize(databaseSizeBeforeUpdate);
        Takeaway testTakeaway = takeawayList.get(takeawayList.size() - 1);
        assertThat(testTakeaway.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
    }

    @Test
    void fullUpdateTakeawayWithPatch() throws Exception {
        // Initialize the database
        takeaway.setId(UUID.randomUUID().toString());
        takeawayRepository.save(takeaway);

        int databaseSizeBeforeUpdate = takeawayRepository.findAll().size();

        // Update the takeaway using partial update
        Takeaway partialUpdatedTakeaway = new Takeaway();
        partialUpdatedTakeaway.setId(takeaway.getId());

        partialUpdatedTakeaway.totalPrice(UPDATED_TOTAL_PRICE);

        restTakeawayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTakeaway.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTakeaway))
            )
            .andExpect(status().isOk());

        // Validate the Takeaway in the database
        List<Takeaway> takeawayList = takeawayRepository.findAll();
        assertThat(takeawayList).hasSize(databaseSizeBeforeUpdate);
        Takeaway testTakeaway = takeawayList.get(takeawayList.size() - 1);
        assertThat(testTakeaway.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    void patchNonExistingTakeaway() throws Exception {
        int databaseSizeBeforeUpdate = takeawayRepository.findAll().size();
        takeaway.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTakeawayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, takeaway.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(takeaway))
            )
            .andExpect(status().isBadRequest());

        // Validate the Takeaway in the database
        List<Takeaway> takeawayList = takeawayRepository.findAll();
        assertThat(takeawayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTakeaway() throws Exception {
        int databaseSizeBeforeUpdate = takeawayRepository.findAll().size();
        takeaway.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTakeawayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(takeaway))
            )
            .andExpect(status().isBadRequest());

        // Validate the Takeaway in the database
        List<Takeaway> takeawayList = takeawayRepository.findAll();
        assertThat(takeawayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTakeaway() throws Exception {
        int databaseSizeBeforeUpdate = takeawayRepository.findAll().size();
        takeaway.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTakeawayMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(takeaway)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Takeaway in the database
        List<Takeaway> takeawayList = takeawayRepository.findAll();
        assertThat(takeawayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTakeaway() throws Exception {
        // Initialize the database
        takeaway.setId(UUID.randomUUID().toString());
        takeawayRepository.save(takeaway);

        int databaseSizeBeforeDelete = takeawayRepository.findAll().size();

        // Delete the takeaway
        restTakeawayMockMvc
            .perform(delete(ENTITY_API_URL_ID, takeaway.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Takeaway> takeawayList = takeawayRepository.findAll();
        assertThat(takeawayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
