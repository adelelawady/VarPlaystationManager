package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Session;
import com.mycompany.myapp.repository.SessionRepository;
import com.mycompany.myapp.service.SessionService;
import com.mycompany.myapp.service.dto.SessionDTO;
import com.mycompany.myapp.service.mapper.SessionMapper;
import java.util.List;
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
        List<Session> session = this.sessionRepository.findByDeviceIdAndActive(deviceId, true);
        Optional<Session> first = session.stream().findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        return null;
    }

    public void stopAllDeviceActiveSessions(String deviceId) {
        List<Session> session = this.sessionRepository.findByDeviceIdAndActive(deviceId, true);
        if (!session.isEmpty()) {
            session
                .stream()
                .forEach(sess -> {
                    sess.setActive(false);
                    sessionRepository.save(sess);
                });
        }
    }

    @Override
    public void addProductOrderToDeviceSession(Session session, Product product) {
        Optional<Session> sessOPt = sessionRepository.findById(session.getId());
        if (!sessOPt.isPresent()) {
            return;
        }
        Session sess = sessOPt.get();
        boolean sessionHasItem = sess.getOrdersQuantity().containsKey(product.getId());
        if (sessionHasItem) {
            int currentvalue = sess.getOrdersQuantity().get(product.getId());
            sess.getOrdersQuantity().put(product.getId(), currentvalue + 1);
        } else {
            sess.getOrdersQuantity().put(product.getId(), 1);
        }
        Session savedSession = sessionRepository.save(sess);

        calculateDeviceSessionOrderesPrice(savedSession);
    }

    @Override
    public void calculateDeviceSessionOrderesPrice(Session session) {
        Double totalCalculationsOfOrders = 0.0;
        for (Product order : session.getOrders()) {
            int prodValue;
            if (session.getOrdersQuantity().containsKey(order.getId())) {
                prodValue = session.getOrdersQuantity().get(order.getId());
            } else {
                prodValue = 1;
            }

            Double prodPrice = order.getPrice();
            Double totalProdPrice = Double.valueOf(prodValue) * prodPrice;
            totalCalculationsOfOrders += totalProdPrice;
        }
        session.setOrdersPrice(totalCalculationsOfOrders);
        sessionRepository.save(session);
    }
}
