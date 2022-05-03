package net.n2oapp.framework.sandbox.cases.mapping_java;


import net.n2oapp.framework.sandbox.cases.persons.Person;
import net.n2oapp.framework.sandbox.cases.persons.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;

    public Person createPerson(Person person) {
        return repository.save(person);
    }

    public Page<Person> getList(PersonCriteria criteria) {
        if (criteria.getFirstName() != null) {
            return repository.findAll(criteria, criteria.getFirstName());
        } else {
            return repository.findAll(criteria);
        }
    }

    public Person getById(Long id) {
        return repository.findById(id).orElse(null) ;
    }


}
