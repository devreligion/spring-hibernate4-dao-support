package com.plaxd.spring.dao.hibernate;

public class MyEntityDAOHibernateImpl extends HibernateDAOSupport<String, my_entity> implements MyEntityDAO{

	public MyEntityDAOHibernateImpl() {
		super(my_entity.class);
	}
}
