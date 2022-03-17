package net.n2oapp.framework.sandbox.server.examples;

import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.framework.sandbox.server.cases.persons.Person;
import net.n2oapp.framework.sandbox.server.cases.persons.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MyService {

    @Autowired
    private PersonRepository repository;

    public Page<Person> getList(Criteria criteria) {
        return repository.findAll(PageRequest.of(criteria.getPage(), criteria.getSize()));
    }

    public Person getOne(Integer id) {
        return repository.findById(Long.valueOf(id)).get();
    }

    public Integer getCount(Criteria criteria) {
        return (int) repository.findAll(PageRequest.of(criteria.getPage(), criteria.getSize())).getTotalElements();
    }

    public Person create(Person p) {
        return repository.save(p);
    }

    public Person update(Person in) {
        Person p = repository.findById(in.getId()).get();
        p.setFirstName(in.getFirstName());
        p.setLastName(in.getLastName());
        repository.save(p);
        return p;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
