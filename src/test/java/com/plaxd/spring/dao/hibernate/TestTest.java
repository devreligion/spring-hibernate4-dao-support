package com.plaxd.spring.dao.hibernate;

import static junit.framework.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class TestTest {

	@Test
	public void simple() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		MyEntityDAO dao = context.getBean(MyEntityDAO.class);
		my_entity testEntity = new my_entity();
		testEntity.id = "1";
		dao.create(testEntity);
		assertNotNull(dao.load("1"));
		context.close();
	}
}
