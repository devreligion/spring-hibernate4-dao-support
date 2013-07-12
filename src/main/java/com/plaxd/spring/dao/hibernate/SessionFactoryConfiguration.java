package com.plaxd.spring.dao.hibernate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionFactoryConfiguration  {
	
	@Bean 
	SessionFactoryInstaller appListener() {
		return new SessionFactoryInstaller();
	}
}
