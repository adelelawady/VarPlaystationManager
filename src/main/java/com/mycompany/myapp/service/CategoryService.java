package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CategoryDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Category}.
 */
public interface CategoryService {
    /**
     * Save a category.
     *
     * @param categoryDTO the entity to save.
     * @return the persisted entity.
     */
    CategoryDTO save(CategoryDTO categoryDTO);

    /**
     * Partially updates a category.
     *
     * @param categoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoryDTO> partialUpdate(CategoryDTO categoryDTO);

    /**
     * Get all the categories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CategoryDTO> findAll(Pageable pageable);

    List<CategoryDTO> findAll();

    /**
     * Get the "id" category.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoryDTO> findOne(String id);

    /**
     * Delete the "id" category.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
