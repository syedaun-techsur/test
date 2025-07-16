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

// https://stackoverflow.com/questions/52517529/how-to-create-schema-in-postgres-db-before-liquibase-start-to-work
@Slf4j
@Configuration
@ConditionalOnClass({ SpringLiquibase.class, DatabaseChange.class })
@ConditionalOnProperty(prefix = "spring.liquibase", name = "enabled", matchIfMissing = true)
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@Import({ LiquibaseSchemaInit.SpringLiquibaseDependsOnPostProcessor.class })
public class LiquibaseSchemaInit {
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

		@Override
		public void afterPropertiesSet() {
			try (Connection conn = dataSource.getConnection(); Statement statement = conn.createStatement()) {
				log.info("Going to create DB schema '{}' if not exists.", schemaName);
				String safeSchemaName = schemaName.replace("\"", "");
				String sql = String.format("create schema if not exists \"%s\"", safeSchemaName);
				statement.execute(sql);
			} catch (SQLException e) {
				throw new RuntimeException("Failed to create schema '" + schemaName + "'", e);
			}
		}
	}

	@ConditionalOnBean(SchemaInitBean.class)
	static class SpringLiquibaseDependsOnPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {

		SpringLiquibaseDependsOnPostProcessor() {
			// Configure the 3rd party SpringLiquibase bean to depend on our SchemaInitBean
			super(SpringLiquibase.class, SchemaInitBean.class);
		}
	}
}