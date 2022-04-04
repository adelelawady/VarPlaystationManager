package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Device;
import com.mycompany.myapp.domain.OrderHandlerDomain;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Session;
import com.mycompany.myapp.repository.DeviceRepository;
import com.mycompany.myapp.repository.ProductRepository;
import com.mycompany.myapp.repository.SessionRepository;
import com.mycompany.myapp.service.SessionService;
import com.mycompany.myapp.service.dto.SessionDTO;
import com.mycompany.myapp.service.dto.SessionEndDTO;
import com.mycompany.myapp.service.dto.SessionStartDTO;
import com.mycompany.myapp.service.mapper.SessionMapper;
import java.util.*;
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

    private final DeviceRepository deviceRepository;

    private final ProductRepository productRepository;

    private final PosService posService;

    public SessionServiceImpl(
        SessionRepository sessionRepository,
        SessionMapper sessionMapper,
        DeviceRepository deviceRepository,
        ProductRepository productRepository,
        PosService posService
    ) {
        this.sessionRepository = sessionRepository;
        this.sessionMapper = sessionMapper;
        this.deviceRepository = deviceRepository;
        this.productRepository = productRepository;
        this.posService = posService;
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

    public void stopAllDeviceActiveSessions(String deviceId) {
        Optional<Device> device = deviceRepository.findById(deviceId);
        if (!device.isPresent()) {
            return;
        }
        Device deviceOp = device.get();
        deviceOp.setSession(null);
        deviceRepository.save(deviceOp);
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
            updateUnOrderdProductsOnInsert(sess, product);
        } else {
            sess.getOrdersQuantity().put(product.getId(), 1);
            updateUnOrderdProductsOnInsert(sess, product);
        }

        Session savedSession = sessionRepository.save(sess);

        calculateDeviceSessionOrderesPrice(savedSession);
    }

    public void updateUnOrderdProductsOnInsert(Session sess, Product product) {
        boolean sessionHasItem = sess.getProductOrderRecord().containsKey(product.getId());
        if (sessionHasItem) {
            int currentvalue = sess.getProductOrderRecord().get(product.getId());
            sess.getProductOrderRecord().put(product.getId(), currentvalue + 1);
            sess.setContainsNewOrders(true);
        } else {
            sess.getProductOrderRecord().put(product.getId(), 1);
            sess.setContainsNewOrders(true);
        }
    }

    @Override
    public void deleteProductOrderFromDeviceSession(Session session, Product product) {
        Optional<Session> sessOPt = sessionRepository.findById(session.getId());
        if (!sessOPt.isPresent()) {
            return;
        }
        Session sess = sessOPt.get();
        boolean sessionHasItem = sess.getOrdersQuantity().containsKey(product.getId());
        if (sessionHasItem) {
            int currentvalue = sess.getOrdersQuantity().get(product.getId());
            if (currentvalue > 1) {
                sess.getOrdersQuantity().put(product.getId(), currentvalue - 1);
                updateUnOrderdProductsOnDelete(sess, product);
            } else {
                sess.getOrdersQuantity().remove(product.getId());
                updateUnOrderdProductsOnDelete(sess, product);
            }
        }
        Session savedSession = sessionRepository.save(sess);

        calculateDeviceSessionOrderesPrice(savedSession);
    }

    @Override
    public void flushOrderSessionUnOrderdProducts(String name, String sessId, boolean print) {
        Optional<Session> sessionOptional = sessionRepository.findById(sessId);

        if (!sessionOptional.isPresent()) {
            return;
        }

        Session sess = sessionOptional.get();
        Set<Product> productSetMarket = new HashSet<>();
        HashMap<String, Integer> productsQuantatiyMarket = new HashMap<>();

        Set<Product> productSetRes = new HashSet<>();
        HashMap<String, Integer> productsQuantatiyRes = new HashMap<>();

        Set<Product> productSetCafe = new HashSet<>();
        HashMap<String, Integer> productsQuantatiyCafe = new HashMap<>();

        if (sess.getProductOrderRecord() == null || sess.getProductOrderRecord().isEmpty()) {
            sess.setContainsNewOrders(false);
            save(sess);
            return;
        }

        Set<String> temp = sess.getProductOrderRecord().keySet();

        for (String productId : temp) {
            Optional<Product> productOptional = productRepository.findById(productId);
            if (!productOptional.isPresent()) {
                continue;
            }
            Product prod = productOptional.get();

            switch (prod.getCategory().getType().toLowerCase()) {
                case "market":
                    {
                        productSetMarket.add(prod);
                        productsQuantatiyMarket.put(prod.getId(), sess.getProductOrderRecord().get(prod.getId()));

                        break;
                    }
                case "res":
                    {
                        productSetRes.add(prod);
                        productsQuantatiyRes.put(prod.getId(), sess.getProductOrderRecord().get(prod.getId()));
                        break;
                    }
                case "cafe":
                    {
                        productSetCafe.add(prod);
                        productsQuantatiyCafe.put(prod.getId(), sess.getProductOrderRecord().get(prod.getId()));
                        break;
                    }
                default:
                    {
                        productSetCafe.add(prod);
                        productsQuantatiyCafe.put(prod.getId(), sess.getProductOrderRecord().get(prod.getId()));
                    }
            }
        }

        sess.getProductOrderRecord().clear();
        sess.setContainsNewOrders(false);
        save(sess);

        if (!productSetMarket.isEmpty()) {
            createPosTableWithOrders(name, sess, OrderHandlerDomain.TABLE_TYPE.MARKET, productSetMarket, productsQuantatiyMarket, print);
        }

        if (!productSetRes.isEmpty()) {
            createPosTableWithOrders(name, sess, OrderHandlerDomain.TABLE_TYPE.RES, productSetRes, productsQuantatiyRes, print);
        }
        if (!productSetCafe.isEmpty()) {
            createPosTableWithOrders(name, sess, OrderHandlerDomain.TABLE_TYPE.CAFE, productSetCafe, productsQuantatiyCafe, print);
        }
    }

    public void createPosTableWithOrders(
        String name,
        Session sess,
        OrderHandlerDomain.TABLE_TYPE type,
        Set<Product> productSet,
        HashMap<String, Integer> productsQuantatiy,
        boolean print
    ) {
        posService.createNewPosOrderAction(name + " - " + sess.getDeviceName(), type, productSet, productsQuantatiy, print);
    }

    public void updateUnOrderdProductsOnDelete(Session sess, Product product) {
        boolean sessionHasItem = sess.getProductOrderRecord().containsKey(product.getId());
        if (sessionHasItem) {
            int currentvalue = sess.getProductOrderRecord().get(product.getId());
            if (currentvalue > 1) {
                sess.getProductOrderRecord().put(product.getId(), currentvalue - 1);
            } else {
                sess.getProductOrderRecord().remove(product.getId());
            }
        }
        if (sess.getProductOrderRecord().isEmpty()) {
            sess.setContainsNewOrders(false);
        }
    }

    @Override
    public void calculateDeviceSessionOrderesPrice(Session session) {
        Double totalCalculationsOfOrders = 0.0;
        for (Product order : session.getOrders()) {
            int prodValue;
            int paiedProdValue;
            if (session.getOrdersQuantity().containsKey(order.getId())) {
                prodValue = session.getOrdersQuantity().get(order.getId());
            } else {
                prodValue = 0;
            }

            if (session.getPaidOrdersQuantity().containsKey(order.getId())) {
                paiedProdValue = session.getPaidOrdersQuantity().get(order.getId());
            } else {
                paiedProdValue = 0;
            }

            Double prodPrice = order.getPrice();
            Double totalProdPrice = Double.valueOf(prodValue) * prodPrice;
            Double totalProdPaiedPrice = Double.valueOf(paiedProdValue) * prodPrice;
            totalCalculationsOfOrders += (totalProdPrice - totalProdPaiedPrice);
        }
        session.setOrdersPrice(totalCalculationsOfOrders);
        sessionRepository.save(session);
    }

    @Override
    public List<Session> findByActive(boolean active) {
        // TODO Auto-generated method stub
        return sessionRepository.findAllByActive(active);
    }

    @Override
    public Optional<Session> findById(String sessionId) {
        return sessionRepository.findById(sessionId);
    }
}
