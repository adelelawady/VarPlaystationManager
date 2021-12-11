package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Session;
import com.mycompany.myapp.repository.SessionRepository;
import com.mycompany.myapp.service.SessionService;
import com.mycompany.myapp.service.dto.SessionDTO;
import com.mycompany.myapp.service.mapper.SessionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Session}.
 */
@Service
public class SessionServiceImpl implements SessionService {

    private final Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);

    private final SessionRepository sessionRepository;

    private final SessionMapper sessionMapper;

    public SessionServiceImpl(SessionRepository sessionRepository, SessionMapper sessionMapper) {
        this.sessionRepository = sessionRepository;
        this.sessionMapper = sessionMapper;
    }

    @Override
    public SessionDTO save(SessionDTO sessionDTO) {
        log.debug("Request to save Session : {}", sessionDTO);
        Session session = sessionMapper.toEntity(sessionDTO);
        session = sessionRepository.save(session);
        return sessionMapper.toDto(session);
    }

    @Override
    public Session save(Session session) {
        session = sessionRepository.save(session);
        return session;
    }

    @Override
    public Optional<SessionDTO> partialUpdate(SessionDTO sessionDTO) {
        log.debug("Request to partially update Session : {}", sessionDTO);

        return sessionRepository
            .findById(sessionDTO.getId())
            .map(existingSession -> {
                sessionMapper.partialUpdate(existingSession, sessionDTO);

                return existingSession;
            })
            .map(sessionRepository::save)
            .map(sessionMapper::toDto);
    }

    @Override
    public Page<SessionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sessions");
        return sessionRepository.findAll(pageable).map(sessionMapper::toDto);
    }

    public Page<SessionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return sessionRepository.findAllWithEagerRelationships(pageable).map(sessionMapper::toDto);
    }

    @Override
    public Optional<SessionDTO> findOne(String id) {
        log.debug("Request to get Session : {}", id);
        return sessionRepository.findOneWithEagerRelationships(id).map(sessionMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Session : {}", id);
        sessionRepository.deleteById(id);
    }

    @Override
    public Session getDeviceActiveSession(String deviceId) {
        Optional<Session> session = this.sessionRepository.findByDeviceIdAndActive(deviceId, true);
        if (session.isPresent()) {
            return session.get();
        }
        return null;
    }
}
