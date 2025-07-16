package solutions.techsur.common.microservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/**
 * Base Data Transfer Object (DTO) class for common entity properties.
 *
 * @param <I> the type of the entity identifier, which must be serializable.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseDto<I extends Serializable> {

    /**
     * Unique identifier of the entity.
     */
    private I id;

    /**
     * Version number for optimistic locking.
     */
    private Integer version;

    /**
     * Timestamp when the entity was created.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Instant created;

    /**
     * Identifier of the user who created the entity.
     */
    private String createdBy;

    /**
     * Name of the user who created the entity.
     */
    private String createdByName;

    /**
     * Timestamp when the entity was last updated.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Instant updated;

    /**
     * Identifier of the user who last updated the entity.
     */
    private String updatedBy;

    /**
     * Name of the user who last updated the entity.
     */
    private String updatedByName;
}