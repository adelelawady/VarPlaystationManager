package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ShopsOrders;
import com.mycompany.myapp.repository.ShopsOrdersRepository;
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
 * Integration tests for the {@link ShopsOrdersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShopsOrdersResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/shops-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ShopsOrdersRepository shopsOrdersRepository;

    @Autowired
    private MockMvc restShopsOrdersMockMvc;

    private ShopsOrders shopsOrders;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopsOrders createEntity() {
        ShopsOrders shopsOrders = new ShopsOrders().name(DEFAULT_NAME).totalPrice(DEFAULT_TOTAL_PRICE);
        return shopsOrders;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopsOrders createUpdatedEntity() {
        ShopsOrders shopsOrders = new ShopsOrders().name(UPDATED_NAME).totalPrice(UPDATED_TOTAL_PRICE);
        return shopsOrders;
    }

    @BeforeEach
    public void initTest() {
        shopsOrdersRepository.deleteAll();
        shopsOrders = createEntity();
    }

    @Test
    void createShopsOrders() throws Exception {
        int databaseSizeBeforeCreate = shopsOrdersRepository.findAll().size();
        // Create the ShopsOrders
        restShopsOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shopsOrders)))
            .andExpect(status().isCreated());

        // Validate the ShopsOrders in the database
        List<ShopsOrders> shopsOrdersList = shopsOrdersRepository.findAll();
        assertThat(shopsOrdersList).hasSize(databaseSizeBeforeCreate + 1);
        ShopsOrders testShopsOrders = shopsOrdersList.get(shopsOrdersList.size() - 1);
        assertThat(testShopsOrders.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testShopsOrders.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
    }

    @Test
    void createShopsOrdersWithExistingId() throws Exception {
        // Create the ShopsOrders with an existing ID
        shopsOrders.setId("existing_id");

        int databaseSizeBeforeCreate = shopsOrdersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopsOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shopsOrders)))
            .andExpect(status().isBadRequest());

        // Validate the ShopsOrders in the database
        List<ShopsOrders> shopsOrdersList = shopsOrdersRepository.findAll();
        assertThat(shopsOrdersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllShopsOrders() throws Exception {
        // Initialize the database
        shopsOrders.setId(UUID.randomUUID().toString());
        shopsOrdersRepository.save(shopsOrders);

        // Get all the shopsOrdersList
        restShopsOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopsOrders.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())));
    }

    @Test
    void getShopsOrders() throws Exception {
        // Initialize the database
        shopsOrders.setId(UUID.randomUUID().toString());
        shopsOrdersRepository.save(shopsOrders);

        // Get the shopsOrders
        restShopsOrdersMockMvc
            .perform(get(ENTITY_API_URL_ID, shopsOrders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shopsOrders.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()));
    }

    @Test
    void getNonExistingShopsOrders() throws Exception {
        // Get the shopsOrders
        restShopsOrdersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewShopsOrders() throws Exception {
        // Initialize the database
        shopsOrders.setId(UUID.randomUUID().toString());
        shopsOrdersRepository.save(shopsOrders);

        int databaseSizeBeforeUpdate = shopsOrdersRepository.findAll().size();

        // Update the shopsOrders
        ShopsOrders updatedShopsOrders = shopsOrdersRepository.findById(shopsOrders.getId()).get();
        updatedShopsOrders.name(UPDATED_NAME).totalPrice(UPDATED_TOTAL_PRICE);

        restShopsOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShopsOrders.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedShopsOrders))
            )
            .andExpect(status().isOk());

        // Validate the ShopsOrders in the database
        List<ShopsOrders> shopsOrdersList = shopsOrdersRepository.findAll();
        assertThat(shopsOrdersList).hasSize(databaseSizeBeforeUpdate);
        ShopsOrders testShopsOrders = shopsOrdersList.get(shopsOrdersList.size() - 1);
        assertThat(testShopsOrders.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShopsOrders.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    void putNonExistingShopsOrders() throws Exception {
        int databaseSizeBeforeUpdate = shopsOrdersRepository.findAll().size();
        shopsOrders.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopsOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shopsOrders.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shopsOrders))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShopsOrders in the database
        List<ShopsOrders> shopsOrdersList = shopsOrdersRepository.findAll();
        assertThat(shopsOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchShopsOrders() throws Exception {
        int databaseSizeBeforeUpdate = shopsOrdersRepository.findAll().size();
        shopsOrders.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopsOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shopsOrders))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShopsOrders in the database
        List<ShopsOrders> shopsOrdersList = shopsOrdersRepository.findAll();
        assertThat(shopsOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamShopsOrders() throws Exception {
        int databaseSizeBeforeUpdate = shopsOrdersRepository.findAll().size();
        shopsOrders.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopsOrdersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shopsOrders)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShopsOrders in the database
        List<ShopsOrders> shopsOrdersList = shopsOrdersRepository.findAll();
        assertThat(shopsOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateShopsOrdersWithPatch() throws Exception {
        // Initialize the database
        shopsOrders.setId(UUID.randomUUID().toString());
        shopsOrdersRepository.save(shopsOrders);

        int databaseSizeBeforeUpdate = shopsOrdersRepository.findAll().size();

        // Update the shopsOrders using partial update
        ShopsOrders partialUpdatedShopsOrders = new ShopsOrders();
        partialUpdatedShopsOrders.setId(shopsOrders.getId());

        partialUpdatedShopsOrders.totalPrice(UPDATED_TOTAL_PRICE);

        restShopsOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShopsOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShopsOrders))
            )
            .andExpect(status().isOk());

        // Validate the ShopsOrders in the database
        List<ShopsOrders> shopsOrdersList = shopsOrdersRepository.findAll();
        assertThat(shopsOrdersList).hasSize(databaseSizeBeforeUpdate);
        ShopsOrders testShopsOrders = shopsOrdersList.get(shopsOrdersList.size() - 1);
        assertThat(testShopsOrders.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testShopsOrders.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    void fullUpdateShopsOrdersWithPatch() throws Exception {
        // Initialize the database
        shopsOrders.setId(UUID.randomUUID().toString());
        shopsOrdersRepository.save(shopsOrders);

        int databaseSizeBeforeUpdate = shopsOrdersRepository.findAll().size();

        // Update the shopsOrders using partial update
        ShopsOrders partialUpdatedShopsOrders = new ShopsOrders();
        partialUpdatedShopsOrders.setId(shopsOrders.getId());

        partialUpdatedShopsOrders.name(UPDATED_NAME).totalPrice(UPDATED_TOTAL_PRICE);

        restShopsOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShopsOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShopsOrders))
            )
            .andExpect(status().isOk());

        // Validate the ShopsOrders in the database
        List<ShopsOrders> shopsOrdersList = shopsOrdersRepository.findAll();
        assertThat(shopsOrdersList).hasSize(databaseSizeBeforeUpdate);
        ShopsOrders testShopsOrders = shopsOrdersList.get(shopsOrdersList.size() - 1);
        assertThat(testShopsOrders.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShopsOrders.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    void patchNonExistingShopsOrders() throws Exception {
        int databaseSizeBeforeUpdate = shopsOrdersRepository.findAll().size();
        shopsOrders.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopsOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shopsOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shopsOrders))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShopsOrders in the database
        List<ShopsOrders> shopsOrdersList = shopsOrdersRepository.findAll();
        assertThat(shopsOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchShopsOrders() throws Exception {
        int databaseSizeBeforeUpdate = shopsOrdersRepository.findAll().size();
        shopsOrders.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopsOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shopsOrders))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShopsOrders in the database
        List<ShopsOrders> shopsOrdersList = shopsOrdersRepository.findAll();
        assertThat(shopsOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamShopsOrders() throws Exception {
        int databaseSizeBeforeUpdate = shopsOrdersRepository.findAll().size();
        shopsOrders.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopsOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(shopsOrders))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShopsOrders in the database
        List<ShopsOrders> shopsOrdersList = shopsOrdersRepository.findAll();
        assertThat(shopsOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteShopsOrders() throws Exception {
        // Initialize the database
        shopsOrders.setId(UUID.randomUUID().toString());
        shopsOrdersRepository.save(shopsOrders);

        int databaseSizeBeforeDelete = shopsOrdersRepository.findAll().size();

        // Delete the shopsOrders
        restShopsOrdersMockMvc
            .perform(delete(ENTITY_API_URL_ID, shopsOrders.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShopsOrders> shopsOrdersList = shopsOrdersRepository.findAll();
        assertThat(shopsOrdersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
