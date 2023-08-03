package net.n2oapp.framework.autotest.cases;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DialogServiceForAT {
    private List<Person> persons;

    public DialogServiceForAT() {
        this.persons = new ArrayList<>(Arrays.asList(
                new Person(1, "test1", 10),
                new Person(2, "test2", 15)));
    }

    public List<Person> findAll() {
        return persons;
    }

    public void create(String name, Integer age) {
        if (name == null)
            throw new IllegalArgumentException("Empty name");
        if (age == null)
            throw new IllegalArgumentException("Empty age");
        persons.add(new Person(persons.size() + 1, name, age));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Person {
        private Integer id;
        private String name;
        private Integer age;
    }
}
