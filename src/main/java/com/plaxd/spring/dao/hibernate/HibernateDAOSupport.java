package com.plaxd.spring.dao.hibernate;

import java.io.Serializable;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateDAOSupport<K extends Serializable, T> {

	private SessionFactory sessionFactory;
	
	private Class<T> clazz;
	
	public HibernateDAOSupport(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	public static interface InSessionAction<R> {
		public R perform(Session session);
	}
	
	public void save(final T object) {
		doInSession(new InSessionAction<Void>() {
			public Void perform(Session session) {
				session.save(object);
				return null;
			}
		});
	}
	
	public <R> R doInSession(InSessionAction<R> action) {
		Session session = sessionFactory.openSession();
		session.setFlushMode(FlushMode.MANUAL);
		try {
			R result = action.perform(session);
			session.flush();
			return result;
		} finally {
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public T load(K key) {
		Session session = getSessionFactory().openSession();
		try {
			return (T) session.get(clazz, key);
		} finally {
			session.close();
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Class<T> getEntityClass() {
		return clazz;
	}
}
