package hello.repository;

import java.util.List;

import hello.model.Person;

public interface LDIFRepository {

//	PersonRepository getPersonRepo();
	
	List<Person> findAll();
	
	//List<Customer> findByLastNameStartsWithIgnoreCase(String lastName);
	
}