package solutions.techsur.common.microservice.config;

import liquibase.change.DatabaseChange;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Configuration class to ensure a database schema exists before Liquibase runs.
 * This avoids errors when Liquibase tries to apply changesets against a non-existent schema.
 */
@Slf4j
@Configuration
@ConditionalOnClass({SpringLiquibase.class, DatabaseChange.class})
@ConditionalOnProperty(prefix = "spring.liquibase", name = "enabled", matchIfMissing = true)
@AutoConfigureAfter({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Import({LiquibaseSchemaInit.SpringLiquibaseDependsOnPostProcessor.class})
public class LiquibaseSchemaInit {

    /**
     * Bean to create the specified schema before Liquibase runs.
     */
    @Component
    @ConditionalOnProperty(prefix = "spring.liquibase", name = "enabled", matchIfMissing = true)
    public static class SchemaInitBean implements InitializingBean {

        private final DataSource dataSource;
        private final String schemaName;

        @Autowired
        public SchemaInitBean(DataSource dataSource, @Value("${spring.liquibase.liquibase-schema}") String schemaName) {
            this.dataSource = dataSource;
            this.schemaName = schemaName;
        }

        /**
         * Creates the schema if it does not exist.
         * Validates the schemaName to avoid SQL issues.
         */
        @Override
        public void afterPropertiesSet() {
            if (schemaName == null || schemaName.trim().isEmpty()) {
                log.warn("Liquibase schema name is not defined or empty; skipping schema creation.");
                return;
            }
            final String safeSchemaName = schemaName.replaceAll("\"", "").trim();
            if (safeSchemaName.isEmpty()) {
                log.warn("Liquibase schema name is empty after sanitization; skipping schema creation.");
                return;
            }

            final String sql = String.format("CREATE SCHEMA IF NOT EXISTS \"%s\"", safeSchemaName);
            try (Connection conn = dataSource.getConnection();
                 Statement statement = conn.createStatement()) {
                log.info("Creating DB schema '{}' if not exists.", safeSchemaName);
                final boolean result = statement.execute(sql);
                log.info("Schema creation executed. Result: {}", result);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create schema '" + safeSchemaName + "'", e);
            }
        }
    }

    /**
     * Ensures that the SpringLiquibase bean depends on the SchemaInitBean,
     * so that schema creation happens before Liquibase runs.
     */
    @ConditionalOnBean(SchemaInitBean.class)
    static class SpringLiquibaseDependsOnPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {

        SpringLiquibaseDependsOnPostProcessor() {
            super(SpringLiquibase.class, SchemaInitBean.class);
        }
    }
}