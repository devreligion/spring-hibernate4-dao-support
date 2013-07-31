spring-hibernate4-dao-support
=============================

Util library for building DAO layer implementation based on spring and hibernate4.

# Features and restrictions

- Automatic discovery of DAO classes. 

- No transaction management overhead.

- No transaction support yet implemented. Autocommit mode.

# Usage

## Define entity class

	@Entity
	public class my_entity {

		@Id
		public String id;
	}

## Define DAO class. You can add own method which access hibernate session.

	public class MyEntityDAO extends HibernateDAOSupport<String, my_entity> {

		public MyEntityDAO() {
			super(my_entity.class);
		}
	}

## Define spring configuration beans (example for Java spring configuration)

	@Bean 
	SessionFactoryInstaller sessionFactoryInstaller() {
		return new SessionFactoryInstaller();
	}

	@Bean
	public MyEntityDAO myEntityDAO() {
		return new MyEntityDAOHibernateImpl();
	}

## Use DAO

	@Autowired
	MyEntityDAO dao;

	public void create(String id) {
		MyEntity entity = new MyEntity();	
		entity.id = id;
		dao.create(entity);
	}

# Required dependencies (maven format)

	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-context</artifactId>
		<version>3.2.0.RELEASE</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-orm</artifactId>
		<version>3.2.0.RELEASE</version>
	</dependency>
	<dependency>
		<groupId>org.hibernate</groupId>
		<artifactId>hibernate-entitymanager</artifactId>
		<version>4.2.3.Final</version>
	</dependency>
