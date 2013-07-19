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

@SuppressWarnings("rawtypes")
public class SessionFactoryInstaller implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
	ApplicationContext appContext;
	
	public void onApplicationEvent(ContextRefreshedEvent event) {
		DataSource dataSource = appContext.getBean(DataSource.class);
		SessionFactoryInstallerWeawer weawer = loadWeawer();
		Collection<HibernateDAOSupport> daos = collectDAOBeans();
		if (daos == null || daos.isEmpty()) {
			return;
		}
		List<Class<?>> entityClasses = collectEntityClasses(daos);
		SessionFactory sessionFactory = createSessionFactory(dataSource, weawer, entityClasses);
		setUPSessionFactory(daos, sessionFactory);
	}

	private SessionFactory createSessionFactory(DataSource dataSource, SessionFactoryInstallerWeawer weawer, List<Class<?>> entityClasses) {
		LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
		localSessionFactoryBean.setDataSource(dataSource);
		localSessionFactoryBean.setAnnotatedClasses(entityClasses.toArray(new Class[0]));
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("connection.autocommit", true);
		if (weawer != null) {
			weawer.configureHibernate(hibernateProperties);
		}
		localSessionFactoryBean.setHibernateProperties(hibernateProperties);
		try {
			localSessionFactoryBean.afterPropertiesSet();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		SessionFactory sessionFactory = localSessionFactoryBean.getObject();
		return sessionFactory;
	}

	private void setUPSessionFactory(Collection<HibernateDAOSupport> daos,
			SessionFactory sessionFactory) {
		for (HibernateDAOSupport<?, ?> dao : daos) {
			dao.setSessionFactory(sessionFactory);
		}
	}

	private SessionFactoryInstallerWeawer loadWeawer() {
		Map<String, SessionFactoryInstallerWeawer> weawers = appContext.getBeansOfType(SessionFactoryInstallerWeawer.class);
		SessionFactoryInstallerWeawer weawer = weawers.isEmpty() ? null : weawers.values().iterator().next();
		return weawer;
	}

	private Collection<HibernateDAOSupport> collectDAOBeans() {
		Map<String, HibernateDAOSupport> daosMap = appContext.getBeansOfType(HibernateDAOSupport.class);
		Collection<HibernateDAOSupport> daos = daosMap.values();
		return daos;
	}

	private List<Class<?>> collectEntityClasses(
			Collection<HibernateDAOSupport> daos) {
		List<Class<?>> entityClasses = new ArrayList<Class<?>>();
		for (HibernateDAOSupport<?, ?> dao : daos) {
			entityClasses.add(dao.getEntityClass());
		}
		return entityClasses;
	}

}
