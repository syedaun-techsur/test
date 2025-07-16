package solutions.techsur.common.microservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import solutions.techsur.common.microservice.utils.JwtUtils;

import java.time.Instant;
import java.util.Objects;

/**
 * Base entity class providing common audit fields and automatic timestamp and username population.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@Builder
@EqualsAndHashCode
@MappedSuperclass
@Access(AccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Audited
public abstract class BaseEntity {

    @Column(name = "created", nullable = false, updatable = false)
    protected Instant created;

    @Column(name = "created_by_name", nullable = false, updatable = false, length = 100)
    protected String createdByName;

    @Column(name = "updated", nullable = false)
    protected Instant updated;

    @Column(name = "updated_by_name", nullable = false, length = 100)
    protected String updatedByName;

    /**
     * Sets creation and update timestamps and usernames before entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.created = now;
        this.updated = now;
        String username = safeGetUsername();
        this.createdByName = username;
        this.updatedByName = username;
    }

    /**
     * Updates the update timestamp and username before entity is updated.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updated = Instant.now();
        this.updatedByName = safeGetUsername();
    }

    /**
     * Gets the current username or returns a default system username if none found.
     *
     * @return username string
     */
    private String safeGetUsername() {
        String username = JwtUtils.getUserName();
        if (Objects.isNull(username) || username.isBlank()) {
            return "SYSTEM";
        }
        return username;
    }
}