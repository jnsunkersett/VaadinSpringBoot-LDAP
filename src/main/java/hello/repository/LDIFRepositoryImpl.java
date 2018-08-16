package hello.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import hello.model.Person;

public class LDIFRepositoryImpl implements LDIFRepository {

	@Autowired
	PersonRepository personRepo;

//	public PersonRepository getPersonRepo() {
//		return personRepo;
//	}
//
//	public void setPersonRepo(PersonRepository personRepo) {
//		this.personRepo = personRepo;
//	}

	@Override
	public List<Person> findAll() {
		return personRepo.findAll();
	}
	
}
