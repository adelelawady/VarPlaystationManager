package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Device;
import com.mycompany.myapp.domain.Session;
import com.mycompany.myapp.repository.SessionRepository;
import com.mycompany.myapp.service.SessionService;
import com.mycompany.myapp.service.dto.SessionDTO;
import com.mycompany.myapp.service.mapper.SessionMapper;
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
 * Integration tests for the {@link SessionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SessionResourceIT {

    private static final Instant DEFAULT_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_RESERVED = 1D;
    private static final Double UPDATED_RESERVED = 2D;

    private static final String ENTITY_API_URL = "/api/sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private SessionRepository sessionRepository;

    @Mock
    private SessionRepository sessionRepositoryMock;

    @Autowired
    private SessionMapper sessionMapper;

    @Mock
    private SessionService sessionServiceMock;

    @Autowired
    private MockMvc restSessionMockMvc;

    private Session session;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Session createEntity() {
        Session session = new Session().start(DEFAULT_START).reserved(DEFAULT_RESERVED);
        // Add required entity
        Device device;
        device = DeviceResourceIT.createEntity();
        device.setId("fixed-id-for-tests");
        session.setDevice(device);
        return session;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Session createUpdatedEntity() {
        Session session = new Session().start(UPDATED_START).reserved(UPDATED_RESERVED);
        // Add required entity
        Device device;
        device = DeviceResourceIT.createUpdatedEntity();
        device.setId("fixed-id-for-tests");
        session.setDevice(device);
        return session;
    }

    @BeforeEach
    public void initTest() {
        sessionRepository.deleteAll();
        session = createEntity();
    }

    @Test
    void createSession() throws Exception {
        int databaseSizeBeforeCreate = sessionRepository.findAll().size();
        // Create the Session
        SessionDTO sessionDTO = sessionMapper.toDto(session);
        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionDTO)))
            .andExpect(status().isCreated());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeCreate + 1);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testSession.getReserved()).isEqualTo(DEFAULT_RESERVED);
    }

    @Test
    void createSessionWithExistingId() throws Exception {
        // Create the Session with an existing ID
        session.setId("existing_id");
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        int databaseSizeBeforeCreate = sessionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSessions() throws Exception {
        // Initialize the database
        session.setId(UUID.randomUUID().toString());
        sessionRepository.save(session);

        // Get all the sessionList
        restSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(session.getId())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].reserved").value(hasItem(DEFAULT_RESERVED.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSessionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(sessionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSessionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sessionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSessionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(sessionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSessionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sessionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getSession() throws Exception {
        // Initialize the database
        session.setId(UUID.randomUUID().toString());
        sessionRepository.save(session);

        // Get the session
        restSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, session.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(session.getId()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START.toString()))
            .andExpect(jsonPath("$.reserved").value(DEFAULT_RESERVED.doubleValue()));
    }

    @Test
    void getNonExistingSession() throws Exception {
        // Get the session
        restSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewSession() throws Exception {
        // Initialize the database
        session.setId(UUID.randomUUID().toString());
        sessionRepository.save(session);

        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Update the session
        Session updatedSession = sessionRepository.findById(session.getId()).get();
        updatedSession.start(UPDATED_START).reserved(UPDATED_RESERVED);
        SessionDTO sessionDTO = sessionMapper.toDto(updatedSession);

        restSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getStart()).isEqualTo(UPDATED_START);
        assertThat(testSession.getReserved()).isEqualTo(UPDATED_RESERVED);
    }

    @Test
    void putNonExistingSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();
        session.setId(UUID.randomUUID().toString());

        // Create the Session
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();
        session.setId(UUID.randomUUID().toString());

        // Create the Session
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();
        session.setId(UUID.randomUUID().toString());

        // Create the Session
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSessionWithPatch() throws Exception {
        // Initialize the database
        session.setId(UUID.randomUUID().toString());
        sessionRepository.save(session);

        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Update the session using partial update
        Session partialUpdatedSession = new Session();
        partialUpdatedSession.setId(session.getId());

        partialUpdatedSession.reserved(UPDATED_RESERVED);

        restSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSession))
            )
            .andExpect(status().isOk());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testSession.getReserved()).isEqualTo(UPDATED_RESERVED);
    }

    @Test
    void fullUpdateSessionWithPatch() throws Exception {
        // Initialize the database
        session.setId(UUID.randomUUID().toString());
        sessionRepository.save(session);

        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Update the session using partial update
        Session partialUpdatedSession = new Session();
        partialUpdatedSession.setId(session.getId());

        partialUpdatedSession.start(UPDATED_START).reserved(UPDATED_RESERVED);

        restSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSession))
            )
            .andExpect(status().isOk());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getStart()).isEqualTo(UPDATED_START);
        assertThat(testSession.getReserved()).isEqualTo(UPDATED_RESERVED);
    }

    @Test
    void patchNonExistingSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();
        session.setId(UUID.randomUUID().toString());

        // Create the Session
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sessionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();
        session.setId(UUID.randomUUID().toString());

        // Create the Session
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();
        session.setId(UUID.randomUUID().toString());

        // Create the Session
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sessionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSession() throws Exception {
        // Initialize the database
        session.setId(UUID.randomUUID().toString());
        sessionRepository.save(session);

        int databaseSizeBeforeDelete = sessionRepository.findAll().size();

        // Delete the session
        restSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, session.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
