package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Verse;
import com.mycompany.myapp.repository.VerseRepository;
import com.mycompany.myapp.service.dto.VerseDTO;
import com.mycompany.myapp.service.mapper.VerseMapper;
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
 * Integration tests for the {@link VerseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VerseResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/verses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private VerseRepository verseRepository;

    @Autowired
    private VerseMapper verseMapper;

    @Autowired
    private MockMvc restVerseMockMvc;

    private Verse verse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Verse createEntity() {
        Verse verse = new Verse().name(DEFAULT_NAME);
        return verse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Verse createUpdatedEntity() {
        Verse verse = new Verse().name(UPDATED_NAME);
        return verse;
    }

    @BeforeEach
    public void initTest() {
        verseRepository.deleteAll();
        verse = createEntity();
    }

    @Test
    void createVerse() throws Exception {
        int databaseSizeBeforeCreate = verseRepository.findAll().size();
        // Create the Verse
        VerseDTO verseDTO = verseMapper.toDto(verse);
        restVerseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(verseDTO)))
            .andExpect(status().isCreated());

        // Validate the Verse in the database
        List<Verse> verseList = verseRepository.findAll();
        assertThat(verseList).hasSize(databaseSizeBeforeCreate + 1);
        Verse testVerse = verseList.get(verseList.size() - 1);
        assertThat(testVerse.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createVerseWithExistingId() throws Exception {
        // Create the Verse with an existing ID
        verse.setId("existing_id");
        VerseDTO verseDTO = verseMapper.toDto(verse);

        int databaseSizeBeforeCreate = verseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVerseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(verseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Verse in the database
        List<Verse> verseList = verseRepository.findAll();
        assertThat(verseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllVerses() throws Exception {
        // Initialize the database
        verse.setId(UUID.randomUUID().toString());
        verseRepository.save(verse);

        // Get all the verseList
        restVerseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(verse.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    void getVerse() throws Exception {
        // Initialize the database
        verse.setId(UUID.randomUUID().toString());
        verseRepository.save(verse);

        // Get the verse
        restVerseMockMvc
            .perform(get(ENTITY_API_URL_ID, verse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(verse.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    void getNonExistingVerse() throws Exception {
        // Get the verse
        restVerseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewVerse() throws Exception {
        // Initialize the database
        verse.setId(UUID.randomUUID().toString());
        verseRepository.save(verse);

        int databaseSizeBeforeUpdate = verseRepository.findAll().size();

        // Update the verse
        Verse updatedVerse = verseRepository.findById(verse.getId()).get();
        updatedVerse.name(UPDATED_NAME);
        VerseDTO verseDTO = verseMapper.toDto(updatedVerse);

        restVerseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, verseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(verseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Verse in the database
        List<Verse> verseList = verseRepository.findAll();
        assertThat(verseList).hasSize(databaseSizeBeforeUpdate);
        Verse testVerse = verseList.get(verseList.size() - 1);
        assertThat(testVerse.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingVerse() throws Exception {
        int databaseSizeBeforeUpdate = verseRepository.findAll().size();
        verse.setId(UUID.randomUUID().toString());

        // Create the Verse
        VerseDTO verseDTO = verseMapper.toDto(verse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVerseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, verseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(verseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Verse in the database
        List<Verse> verseList = verseRepository.findAll();
        assertThat(verseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchVerse() throws Exception {
        int databaseSizeBeforeUpdate = verseRepository.findAll().size();
        verse.setId(UUID.randomUUID().toString());

        // Create the Verse
        VerseDTO verseDTO = verseMapper.toDto(verse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVerseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(verseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Verse in the database
        List<Verse> verseList = verseRepository.findAll();
        assertThat(verseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamVerse() throws Exception {
        int databaseSizeBeforeUpdate = verseRepository.findAll().size();
        verse.setId(UUID.randomUUID().toString());

        // Create the Verse
        VerseDTO verseDTO = verseMapper.toDto(verse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVerseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(verseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Verse in the database
        List<Verse> verseList = verseRepository.findAll();
        assertThat(verseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateVerseWithPatch() throws Exception {
        // Initialize the database
        verse.setId(UUID.randomUUID().toString());
        verseRepository.save(verse);

        int databaseSizeBeforeUpdate = verseRepository.findAll().size();

        // Update the verse using partial update
        Verse partialUpdatedVerse = new Verse();
        partialUpdatedVerse.setId(verse.getId());

        partialUpdatedVerse.name(UPDATED_NAME);

        restVerseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVerse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVerse))
            )
            .andExpect(status().isOk());

        // Validate the Verse in the database
        List<Verse> verseList = verseRepository.findAll();
        assertThat(verseList).hasSize(databaseSizeBeforeUpdate);
        Verse testVerse = verseList.get(verseList.size() - 1);
        assertThat(testVerse.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void fullUpdateVerseWithPatch() throws Exception {
        // Initialize the database
        verse.setId(UUID.randomUUID().toString());
        verseRepository.save(verse);

        int databaseSizeBeforeUpdate = verseRepository.findAll().size();

        // Update the verse using partial update
        Verse partialUpdatedVerse = new Verse();
        partialUpdatedVerse.setId(verse.getId());

        partialUpdatedVerse.name(UPDATED_NAME);

        restVerseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVerse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVerse))
            )
            .andExpect(status().isOk());

        // Validate the Verse in the database
        List<Verse> verseList = verseRepository.findAll();
        assertThat(verseList).hasSize(databaseSizeBeforeUpdate);
        Verse testVerse = verseList.get(verseList.size() - 1);
        assertThat(testVerse.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingVerse() throws Exception {
        int databaseSizeBeforeUpdate = verseRepository.findAll().size();
        verse.setId(UUID.randomUUID().toString());

        // Create the Verse
        VerseDTO verseDTO = verseMapper.toDto(verse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVerseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, verseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(verseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Verse in the database
        List<Verse> verseList = verseRepository.findAll();
        assertThat(verseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchVerse() throws Exception {
        int databaseSizeBeforeUpdate = verseRepository.findAll().size();
        verse.setId(UUID.randomUUID().toString());

        // Create the Verse
        VerseDTO verseDTO = verseMapper.toDto(verse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVerseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(verseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Verse in the database
        List<Verse> verseList = verseRepository.findAll();
        assertThat(verseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamVerse() throws Exception {
        int databaseSizeBeforeUpdate = verseRepository.findAll().size();
        verse.setId(UUID.randomUUID().toString());

        // Create the Verse
        VerseDTO verseDTO = verseMapper.toDto(verse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVerseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(verseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Verse in the database
        List<Verse> verseList = verseRepository.findAll();
        assertThat(verseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteVerse() throws Exception {
        // Initialize the database
        verse.setId(UUID.randomUUID().toString());
        verseRepository.save(verse);

        int databaseSizeBeforeDelete = verseRepository.findAll().size();

        // Delete the verse
        restVerseMockMvc
            .perform(delete(ENTITY_API_URL_ID, verse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Verse> verseList = verseRepository.findAll();
        assertThat(verseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
