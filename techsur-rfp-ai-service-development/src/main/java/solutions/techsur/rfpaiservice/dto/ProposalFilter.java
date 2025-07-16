package solutions.techsur.rfpaiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Filter criteria for querying proposals.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProposalFilter extends CommonFilter {

    /**
     * Indicates whether to filter archived proposals.
     * If null, no filtering on archived status will be applied.
     */
    private Boolean isArchived = false;
}