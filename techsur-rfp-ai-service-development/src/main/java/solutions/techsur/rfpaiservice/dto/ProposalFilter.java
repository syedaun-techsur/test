package solutions.techsur.rfpaiservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProposalFilter extends CommonFilter{
    private boolean isArchived;
}
