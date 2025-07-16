package solutions.techsur.common.microservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseDto<I> {
	protected I id;
	protected Integer version;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	protected Instant created;
	protected String createdBy;
	protected String createdByName;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	protected Instant updated;
	protected String updatedBy;
	protected String updatedByName;
}
