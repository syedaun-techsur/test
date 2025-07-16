package solutions.techsur.common.microservice.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

/**
 * Base entity with auto-incrementing ID.
 * Provides an ID field with GenerationType.IDENTITY strategy.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Audited
public abstract class AutoIncrementingBaseEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}