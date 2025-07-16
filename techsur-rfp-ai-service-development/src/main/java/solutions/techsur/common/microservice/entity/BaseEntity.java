package solutions.techsur.common.microservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import solutions.techsur.common.microservice.utils.JwtUtils;

import java.time.Instant;

@Getter
@Setter
@ToString
@SuperBuilder
@EqualsAndHashCode
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Audited
public abstract class BaseEntity {

	@Column(name = "created", nullable = false, updatable = false)
	protected Instant created;

	@Column(name = "created_by_name", nullable = false, updatable = false)
	protected String createdByName;

	@Column(name = "updated", nullable = false)
	protected Instant updated;

	@Column(name = "updated_by_name", nullable = false)
	protected String updatedByName;

	@PrePersist
	protected void onCreate() {
		this.created = Instant.now();
		this.updated = this.created;
		this.createdByName = JwtUtils.getUserName();
		this.updatedByName = this.createdByName;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updated = Instant.now();
		this.updatedByName = JwtUtils.getUserName();
	}
}
