package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Verses;
import com.mycompany.myapp.repository.VersesRepository;
import com.mycompany.myapp.service.dto.VersesDTO;
import com.mycompany.myapp.service.mapper.VersesMapper;
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
 * Integration tests for the {@link VersesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VersesResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/verses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private VersesRepository versesRepository;

    @Autowired
    private VersesMapper versesMapper;

    @Autowired
    private MockMvc restVersesMockMvc;

    private Verses verses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Verses createEntity() {
        Verses verses = new Verses().text(DEFAULT_TEXT);
        return verses;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Verses createUpdatedEntity() {
        Verses verses = new Verses().text(UPDATED_TEXT);
        return verses;
    }

    @BeforeEach
    public void initTest() {
        versesRepository.deleteAll();
        verses = createEntity();
    }

    @Test
    void createVerses() throws Exception {
        int databaseSizeBeforeCreate = versesRepository.findAll().size();
        // Create the Verses
        VersesDTO versesDTO = versesMapper.toDto(verses);
        restVersesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(versesDTO)))
            .andExpect(status().isCreated());

        // Validate the Verses in the database
        List<Verses> versesList = versesRepository.findAll();
        assertThat(versesList).hasSize(databaseSizeBeforeCreate + 1);
        Verses testVerses = versesList.get(versesList.size() - 1);
        assertThat(testVerses.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    void createVersesWithExistingId() throws Exception {
        // Create the Verses with an existing ID
        verses.setId("existing_id");
        VersesDTO versesDTO = versesMapper.toDto(verses);

        int databaseSizeBeforeCreate = versesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVersesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(versesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Verses in the database
        List<Verses> versesList = versesRepository.findAll();
        assertThat(versesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllVerses() throws Exception {
        // Initialize the database
        verses.setId(UUID.randomUUID().toString());
        versesRepository.save(verses);

        // Get all the versesList
        restVersesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(verses.getId())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)));
    }

    @Test
    void getVerses() throws Exception {
        // Initialize the database
        verses.setId(UUID.randomUUID().toString());
        versesRepository.save(verses);

        // Get the verses
        restVersesMockMvc
            .perform(get(ENTITY_API_URL_ID, verses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(verses.getId()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT));
    }

    @Test
    void getNonExistingVerses() throws Exception {
        // Get the verses
        restVersesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewVerses() throws Exception {
        // Initialize the database
        verses.setId(UUID.randomUUID().toString());
        versesRepository.save(verses);

        int databaseSizeBeforeUpdate = versesRepository.findAll().size();

        // Update the verses
        Verses updatedVerses = versesRepository.findById(verses.getId()).get();
        updatedVerses.text(UPDATED_TEXT);
        VersesDTO versesDTO = versesMapper.toDto(updatedVerses);

        restVersesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, versesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(versesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Verses in the database
        List<Verses> versesList = versesRepository.findAll();
        assertThat(versesList).hasSize(databaseSizeBeforeUpdate);
        Verses testVerses = versesList.get(versesList.size() - 1);
        assertThat(testVerses.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    void putNonExistingVerses() throws Exception {
        int databaseSizeBeforeUpdate = versesRepository.findAll().size();
        verses.setId(UUID.randomUUID().toString());

        // Create the Verses
        VersesDTO versesDTO = versesMapper.toDto(verses);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVersesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, versesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(versesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Verses in the database
        List<Verses> versesList = versesRepository.findAll();
        assertThat(versesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchVerses() throws Exception {
        int databaseSizeBeforeUpdate = versesRepository.findAll().size();
        verses.setId(UUID.randomUUID().toString());

        // Create the Verses
        VersesDTO versesDTO = versesMapper.toDto(verses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVersesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(versesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Verses in the database
        List<Verses> versesList = versesRepository.findAll();
        assertThat(versesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamVerses() throws Exception {
        int databaseSizeBeforeUpdate = versesRepository.findAll().size();
        verses.setId(UUID.randomUUID().toString());

        // Create the Verses
        VersesDTO versesDTO = versesMapper.toDto(verses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVersesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(versesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Verses in the database
        List<Verses> versesList = versesRepository.findAll();
        assertThat(versesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateVersesWithPatch() throws Exception {
        // Initialize the database
        verses.setId(UUID.randomUUID().toString());
        versesRepository.save(verses);

        int databaseSizeBeforeUpdate = versesRepository.findAll().size();

        // Update the verses using partial update
        Verses partialUpdatedVerses = new Verses();
        partialUpdatedVerses.setId(verses.getId());

        restVersesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVerses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVerses))
            )
            .andExpect(status().isOk());

        // Validate the Verses in the database
        List<Verses> versesList = versesRepository.findAll();
        assertThat(versesList).hasSize(databaseSizeBeforeUpdate);
        Verses testVerses = versesList.get(versesList.size() - 1);
        assertThat(testVerses.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    void fullUpdateVersesWithPatch() throws Exception {
        // Initialize the database
        verses.setId(UUID.randomUUID().toString());
        versesRepository.save(verses);

        int databaseSizeBeforeUpdate = versesRepository.findAll().size();

        // Update the verses using partial update
        Verses partialUpdatedVerses = new Verses();
        partialUpdatedVerses.setId(verses.getId());

        partialUpdatedVerses.text(UPDATED_TEXT);

        restVersesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVerses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVerses))
            )
            .andExpect(status().isOk());

        // Validate the Verses in the database
        List<Verses> versesList = versesRepository.findAll();
        assertThat(versesList).hasSize(databaseSizeBeforeUpdate);
        Verses testVerses = versesList.get(versesList.size() - 1);
        assertThat(testVerses.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    void patchNonExistingVerses() throws Exception {
        int databaseSizeBeforeUpdate = versesRepository.findAll().size();
        verses.setId(UUID.randomUUID().toString());

        // Create the Verses
        VersesDTO versesDTO = versesMapper.toDto(verses);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVersesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, versesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(versesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Verses in the database
        List<Verses> versesList = versesRepository.findAll();
        assertThat(versesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchVerses() throws Exception {
        int databaseSizeBeforeUpdate = versesRepository.findAll().size();
        verses.setId(UUID.randomUUID().toString());

        // Create the Verses
        VersesDTO versesDTO = versesMapper.toDto(verses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVersesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(versesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Verses in the database
        List<Verses> versesList = versesRepository.findAll();
        assertThat(versesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamVerses() throws Exception {
        int databaseSizeBeforeUpdate = versesRepository.findAll().size();
        verses.setId(UUID.randomUUID().toString());

        // Create the Verses
        VersesDTO versesDTO = versesMapper.toDto(verses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVersesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(versesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Verses in the database
        List<Verses> versesList = versesRepository.findAll();
        assertThat(versesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteVerses() throws Exception {
        // Initialize the database
        verses.setId(UUID.randomUUID().toString());
        versesRepository.save(verses);

        int databaseSizeBeforeDelete = versesRepository.findAll().size();

        // Delete the verses
        restVersesMockMvc
            .perform(delete(ENTITY_API_URL_ID, verses.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Verses> versesList = versesRepository.findAll();
        assertThat(versesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
