package solutions.techsur.rfpaiservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object for creation operations.
 */
@Data
@Builder
public class CreationDTO {
    /**
     * Identifier of the entity.
     */
    private Integer id;
}