package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.Product;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Record} entity.
 */
public class RecordsFilterRequestDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String queryType;

    private Instant from;

    private Instant to;

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public Instant getFrom() {
        return from;
    }

    public void setFrom(Instant from) {
        this.from = from;
    }

    public Instant getTo() {
        return to;
    }

    public void setTo(Instant to) {
        this.to = to;
    }
}
