package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.repository.ProductRepository;
import com.mycompany.myapp.service.ProductService;
import com.mycompany.myapp.service.dto.ProductDTO;
import com.mycompany.myapp.service.mapper.ProductMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
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
    public Optional<ProductDTO> partialUpdate(ProductDTO productDTO) {
        log.debug("Request to partially update Product : {}", productDTO);

        return productRepository
            .findById(productDTO.getId())
            .map(existingProduct -> {
                productMapper.partialUpdate(existingProduct, productDTO);

                return existingProduct;
            })
            .map(productRepository::save)
            .map(productMapper::toDto);
    }

    @Override
    public Page<ProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable).map(productMapper::toDto);
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
        return productRepository.findByCategoryId(categoryId).stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findOneDomain(String id) {
        return productRepository.findById(id);
    }
}
