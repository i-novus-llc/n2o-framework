package net.n2oapp.framework.sandbox.cases.mapping_java;


import net.n2oapp.framework.sandbox.cases.persons.Person;
import net.n2oapp.framework.sandbox.cases.persons.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;

    public Person createPerson(Person person) {
        return repository.save(person);
    }

    public Page<Person> getList(PersonCriteria criteria) {
        PageRequest pageRequest = PageRequest.of(criteria.getPage(), criteria.getSize());
        if (criteria.getFirstName() != null) {
            return repository.findAll(pageRequest, criteria.getFirstName());
        } else {
            return repository.findAll(pageRequest);
        }
    }

    public Person getById(Long id) {
        return repository.findById(id).orElse(null) ;
    }


}
