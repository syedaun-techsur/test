package solutions.techsur.rfpaiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Filter object to use when querying proposals.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalFilter extends CommonFilter {
    /**
     * Indicates whether the proposal is archived.
     */
    private boolean archived;

    private static final long serialVersionUID = 1L;
}