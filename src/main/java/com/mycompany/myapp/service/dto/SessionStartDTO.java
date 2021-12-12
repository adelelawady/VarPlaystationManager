package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Session} entity.
 */
public class SessionStartDTO implements Serializable {

    private Double reserved;

    private boolean multi;

    public Double getReserved() {
        return reserved;
    }

    public void setReserved(Double reserved) {
        this.reserved = reserved;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }
}
