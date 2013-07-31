package com.plaxd.spring.dao.hibernate;

public interface MyEntityDAO {

	
	public void create(my_entity testEntity);
	
	public my_entity load(String id);
}
