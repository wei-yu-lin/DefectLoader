package wistron.defectEdx.util.config;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryDefectEdx", transactionManagerRef = "transactionManagerDefectEdx", basePackages = {
		"wistron.defectEdx.repository.defectEdx" })
public class DefectEdxConfig {
	@Autowired
	@Qualifier("defectEdxDataSource")
	private DataSource defectEdxDataSource;		
	@Autowired
	@Qualifier("vendorProperties")
	private Map<String, Object> vendorProperties;
	@Primary
	@Bean(name = "entityManagerFactoryDefectEdx")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryDefectEdx(
			EntityManagerFactoryBuilder builder) {
		return builder
		.dataSource(defectEdxDataSource)
		.properties(vendorProperties)
		.packages("wistron.defectEdx.db.defectEdxDataTable")
		.persistenceUnit("levelingPersistenceUnit").build();
	}
	@Primary
	@Bean(name = "entityManagerFactoryDefectEdx")
	public EntityManager entityManagerDefectEdx(EntityManagerFactoryBuilder builder) {
		return entityManagerFactoryDefectEdx(builder).getObject().createEntityManager();
	}
	@Primary
	@Bean(name = "transactionManagerDefectEdx")
	PlatformTransactionManager transactionManagerDefectEdx(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(entityManagerFactoryDefectEdx(builder).getObject());
	}
}
