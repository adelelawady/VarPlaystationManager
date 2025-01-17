package com.mycompany.myapp.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.repository.ProductRepository;
import com.mycompany.myapp.service.ProductService;
import com.mycompany.myapp.service.dto.FromToDTO;
import com.mycompany.myapp.service.dto.ProductDTO;
import com.mycompany.myapp.service.dto.ProductStaticsDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Product}.
 */
@RestController
@RequestMapping("/api")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "product";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RestTemplate restTemplate;

    private final ProductService productService;

    private final ProductRepository productRepository;

    public ProductResource(ProductService productService, ProductRepository productRepository, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.productService = productService;
        this.productRepository = productRepository;
    }

    /**
     * {@code POST  /products} : Create a new product.
     *
     * @param productDTO the productDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new productDTO, or with status {@code 400 (Bad Request)} if
     *         the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) throws URISyntaxException {
        log.debug("REST request to save Product : {}", productDTO);
        if (productDTO.getId() != null) {
            throw new BadRequestAlertException("A new product cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductDTO result = productService.save(productDTO);
        return ResponseEntity
            .created(new URI("/api/products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /products/:id} : Updates an existing product.
     *
     * @param id         the id of the productDTO to save.
     * @param productDTO the productDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated productDTO, or with status {@code 400 (Bad Request)} if
     *         the productDTO is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the productDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ProductDTO productDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Product : {}, {}", id, productDTO);
        if (productDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductDTO result = productService.save(productDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /products/:id} : Partial updates given fields of an existing
     * product, field will ignore if it is null
     *
     * @param id         the id of the productDTO to save.
     * @param productDTO the productDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated productDTO, or with status {@code 400 (Bad Request)} if
     *         the productDTO is not valid, or with status {@code 404 (Not Found)}
     *         if the productDTO is not found, or with status
     *         {@code 500 (Internal Server Error)} if the productDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductDTO> partialUpdateProduct(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ProductDTO productDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Product partially : {}, {}", id, productDTO);
        if (productDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductDTO> result = productService.partialUpdate(productDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productDTO.getId())
        );
    }

    /**
     * {@code GET  /products} : get all the products.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of products in body.
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts(Pageable pageable) {
        log.debug("REST request to get a page of Products");
        Page<ProductDTO> page = productService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /products/:id} : get the "id" product.
     *
     * @param id the id of the productDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the productDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable String id) {
        log.debug("REST request to get Product : {}", id);
        Optional<ProductDTO> productDTO = productService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productDTO);
    }

    /**
     * {@code DELETE  /products/:id} : delete the "id" product.
     *
     * @param id the id of the productDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        log.debug("REST request to delete Product : {}", id);
        productService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    public String tranlateArabicTxt(String txt) {
        try {
            String url = "https://translation.googleapis.com/language/translate/v2?key=";

            // create headers
            HttpHeaders headers = new HttpHeaders();
            // set `content-type` header
            headers.setContentType(MediaType.APPLICATION_JSON);
            // set `accept` header
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add(
                "user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36"
            );
            // create a map for post parameters
            Map<String, Object> map = new HashMap<>();
            map.put("key", "");
            map.put("q", txt);
            map.put("source", "ar");
            map.put("target", "en");
            map.put("format", "text");

            // build the request
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

            // send POST request
            ResponseEntity<String> response = this.restTemplate.postForEntity(url, entity, String.class);

            // check response status code
            // org.json.JSONObject jsonObject;
            // jsonObject = new org.json.JSONObject(response.getBody());
            // return jsonObject.getJSONObject("data").getJSONArray("translations").getJSONObject(0).get("translatedText").toString();
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/products/translate-products")
    public ResponseEntity<String> tranlateProducts() {
        log.debug("REST request to get a page of Products");
        for (Product prod : productRepository.findAll()) {
            String enTxt = tranlateArabicTxt(prod.getName());
            if (enTxt == null) {}
            prod.setEnName(enTxt);
            productRepository.save(prod);
        }

        return ResponseEntity.ok("done");
    }

    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getAllProductsByCategory(@PathVariable String categoryId) {
        log.debug("REST request to get a page of Products");
        List<ProductDTO> prodList = productService.findAllByCategory(categoryId);
        return ResponseEntity.ok(prodList);
    }

    @PostMapping("/products/products-stats/{sort}")
    public ResponseEntity<List<ProductStaticsDTO>> findProduct(
        Pageable pageable,
        @PathVariable int sort,
        @RequestBody FromToDTO fromToDto
    ) {
        System.out.println("from : " + fromToDto.getFrom());
        System.out.println("to : " + fromToDto.getTo());

        List<ProductStaticsDTO> x = productRepository
            .findAll()
            .stream()
            .map(prod -> {
                return this.productService.getProductTotalUses(
                        prod.getId(),
                        fromToDto.getFrom().toInstant(),
                        fromToDto.getTo().toInstant()
                    );
            })
            .collect(Collectors.toList());

        Collections.sort(
            x,
            new Comparator<ProductStaticsDTO>() {
                @Override
                public int compare(ProductStaticsDTO u1, ProductStaticsDTO u2) {
                    if (sort == 1) {
                        return Integer.compare(u1.getUseCount(), u2.getUseCount());
                    }
                    if (sort == 0) {
                        return u1.getUsePrice().compareTo(u2.getUsePrice());
                    }
                    return u1.getUsePrice().compareTo(u2.getUsePrice());
                }
            }
                .reversed()
        );

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), x.size());
        final Page<ProductStaticsDTO> page = new PageImpl<>(x.subList(start, end), pageable, x.size());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
