package com.plaxd.spring.dao.hibernate;

public interface MyEntityDAO {

	
	public void save(my_entity testEntity);
	
	public my_entity load(String id);
}
