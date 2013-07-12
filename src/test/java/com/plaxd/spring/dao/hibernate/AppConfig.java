package com.plaxd.spring.dao.hibernate;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Import(SessionFactoryConfiguration.class)
public class AppConfig {

	@Bean
	public DataSource dataSource() {
		return new DriverManagerDataSource( 
				"jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
				"sa",
				"sa");
	}
	
	@Bean
	public SessionFactoryInstallerWeawer generateTables() {
		return new SessionFactoryInstallerWeawer() {
			public void configureHibernate(Properties properties) {
				properties.put("hibernate.hbm2ddl.auto", "create");
			}
		};
	}
	
	@Bean
	public MyEntityDAO testEntityDAO() {
		return new MyEntityDAOHibernateImpl();
	}
	
}
