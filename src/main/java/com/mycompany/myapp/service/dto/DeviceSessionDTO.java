package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.Session;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Device} entity.
 */
public class DeviceSessionDTO implements Serializable {

    private String id;

    private String name;

    private String type;

    private Session session;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceSessionDTO)) {
            return false;
        }

        DeviceSessionDTO deviceDTO = (DeviceSessionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", type=" + getType() +
            "}";
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
