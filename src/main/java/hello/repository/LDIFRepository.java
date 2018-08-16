package hello.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import hello.model.Person;

@Service
public interface LDIFRepository {

//	PersonRepository getPersonRepo();
	
	List<Person> findAll();
	
	//List<Customer> findByLastNameStartsWithIgnoreCase(String lastName);
	
}