package solutions.techsur.rfpaiservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Data Transfer Object for Creation entity.
 */
@Data
@Builder
public class CreationDTO {
    @NonNull
    private Integer id;
}