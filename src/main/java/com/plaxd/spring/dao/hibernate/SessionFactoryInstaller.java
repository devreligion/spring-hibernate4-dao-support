package com.plaxd.spring.dao.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

public class SessionFactoryInstaller implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
	ApplicationContext appContext;
	
	@Autowired
	DataSource dataSource;
	
	@SuppressWarnings("rawtypes")
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Map<String, HibernateDAOSupport> name2DAO = appContext.getBeansOfType(HibernateDAOSupport.class);
		Collection<HibernateDAOSupport> daos = name2DAO.values();
		
		if (daos == null || daos.isEmpty()) {
			return;
		}
		List<Class<?>> entityClasses = new ArrayList<Class<?>>();
		for (HibernateDAOSupport<?, ?> dao : daos) {
			entityClasses.add(dao.getEntityClass());
		}
		LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
		localSessionFactoryBean.setDataSource(dataSource);
		localSessionFactoryBean.setAnnotatedClasses(entityClasses.toArray(new Class[0]));
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("connection.autocommit", true);
		localSessionFactoryBean.setHibernateProperties(hibernateProperties);
		try {
			localSessionFactoryBean.afterPropertiesSet();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		SessionFactory sessionFactory = localSessionFactoryBean.getObject();
		for (HibernateDAOSupport<?, ?> dao : daos) {
			dao.setSessionFactory(sessionFactory);
		}
	}

}
