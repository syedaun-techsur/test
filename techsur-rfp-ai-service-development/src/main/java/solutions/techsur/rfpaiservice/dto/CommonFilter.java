package solutions.techsur.rfpaiservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@ParameterObject
public class CommonFilter {
    private String search;
}
