package wistron.defectEdx.util.config;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
	@Autowired
	private JpaProperties jpaProperties;
	@Autowired
	private HibernateProperties hibernateProperties;
	
	@Primary
	@Bean(name = "defectEdxDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.defect-edx")
	public DataSource DefectEdxDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "vendorProperties")
	public Map<String, Object> getVendorProperties() {
		return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
	}
	
}