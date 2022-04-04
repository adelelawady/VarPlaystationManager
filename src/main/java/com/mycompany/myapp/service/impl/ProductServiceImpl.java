package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.TableRecord;
import com.mycompany.myapp.repository.ProductRepository;
import com.mycompany.myapp.repository.RecordRepository;
import com.mycompany.myapp.repository.TableRecordRepository;
import com.mycompany.myapp.service.ProductService;
import com.mycompany.myapp.service.dto.ProductDTO;
import com.mycompany.myapp.service.dto.ProductStaticsDTO;
import com.mycompany.myapp.service.mapper.ProductMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;
    private final TableRecordRepository tableRecordRepository;
    private final RecordRepository recordRepository;

    public ProductServiceImpl(
        ProductRepository productRepository,
        ProductMapper productMapper,
        RecordRepository recordRepository,
        TableRecordRepository tableRecordRepository
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.tableRecordRepository = tableRecordRepository;
        this.recordRepository = recordRepository;
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public Product save(Product product) {
        product = productRepository.save(product);
        return product;
    }

    @Override
    public Page<ProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findByHasParent(pageable, false).map(productMapper::toDto);
    }

    @Override
    public Optional<ProductDTO> findOne(String id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id).map(productMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> findAllByCategory(String categoryId) {
        return productRepository
            .findByHasParentAndCategoryId(false, categoryId)
            .stream()
            .map(productMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findOneDomain(String id) {
        return productRepository.findById(id);
    }

    public ProductStaticsDTO getProductTotalUses(String productId, Instant from, Instant to) {
        List<com.mycompany.myapp.domain.Record> recordsList = new ArrayList<>();

        List<TableRecord> recordsTableList = new ArrayList<>();

        if (from != null && to != null) {
            recordsList = this.recordRepository.findAllByOrdersDataIdAndEndBetween(productId, from, to);
            recordsTableList = this.tableRecordRepository.findAllByOrdersDataIdAndCreatedDateBetween(productId, from, to);
        } else {
            recordsList = this.recordRepository.findAllByOrdersDataId(productId);
            recordsTableList = this.tableRecordRepository.findAllByOrdersDataId(productId);
        }

        ProductStaticsDTO prodStat = new ProductStaticsDTO();
        Double totalProductPriceInAll = 0.0;
        int totalProductCountInAll = 0;

        for (com.mycompany.myapp.domain.Record rec : recordsList) {
            if (rec.getOrdersQuantity().containsKey(productId)) {
                int count = rec.getOrdersQuantity().get(productId);
                totalProductCountInAll += count;
                Product prodx = rec.getOrdersData().stream().filter(prod -> prod.getId().equals(productId)).findFirst().get();

                totalProductPriceInAll += prodx.getPrice();
            }
        }

        for (TableRecord rec : recordsTableList) {
            if (rec.getOrdersQuantity().containsKey(productId)) {
                int count = rec.getOrdersQuantity().get(productId);
                totalProductCountInAll += count;
                Product prodx = rec.getOrdersData().stream().filter(prod -> prod.getId().equals(productId)).findFirst().get();

                switch (rec.getType()) {
                    case TABLE:
                        totalProductPriceInAll += prodx.getPrice();
                        break;
                    case TAKEAWAY:
                        totalProductPriceInAll += prodx.getTakeawayPrice();
                        break;
                    case SHOPS:
                        totalProductPriceInAll += prodx.getShopsPrice();
                        break;
                    default:
                        break;
                }
            }
        }
        Optional<Product> prodFound = productRepository.findById(productId);
        if (prodFound.isPresent()) {
            prodStat.setId(productId);
            prodStat.setUseCount(totalProductCountInAll);
            prodStat.setUsePrice(totalProductPriceInAll);
            prodStat.setName(prodFound.get().getName());
        }
        return prodStat;
    }

    @Override
    public void addSubProduct(String productId, ProductDTO prod) {
        Optional<Product> prodFound = productRepository.findById(productId);
        if (prodFound.isPresent()) {
            Product prodCreated = productRepository.save(productMapper.toEntity(prod));
            prodCreated.setHasParent(true);
            prodCreated.setParent(productId);
            Product prodCreatedAfterUpdate = productRepository.save(prodCreated);
            Product prodToUpdate = prodFound.get();
            prodToUpdate.getSubProducts().add(prodCreatedAfterUpdate);
            productRepository.save(prodToUpdate);
        }
    }

    @Override
    public void removeSubProduct(String productId, String subProductId) {
        Optional<Product> prodFound = productRepository.findById(productId);
        if (prodFound.isPresent()) {
            Optional<Product> prodSubFound = productRepository.findById(subProductId);
            prodFound.get().getSubProducts().remove(prodSubFound.get());
            Product prodToUpdate = prodFound.get();
            prodToUpdate.getSubProducts().remove(prodSubFound.get());
            productRepository.save(prodToUpdate);
            productRepository.deleteById(subProductId);
        }
    }
}
