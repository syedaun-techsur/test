package solutions.techsur.common.microservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import org.hibernate.envers.Audited;
import solutions.techsur.common.microservice.utils.JwtUtils;

import java.time.Instant;

/**
 * BaseEntity is an abstract JPA mapped superclass that provides audit fields
 * such as created/updated timestamps and usernames.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@EqualsAndHashCode
@MappedSuperclass
@Access(AccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
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
     * Lifecycle callback to set created and updated fields before persisting.
     */
    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.created = now;
        this.updated = now;
        String userName = JwtUtils.getUserName();
        this.createdByName = userName;
        this.updatedByName = userName;
    }

    /**
     * Lifecycle callback to update updated fields before updating.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updated = Instant.now();
        this.updatedByName = JwtUtils.getUserName();
    }
}