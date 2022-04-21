package net.n2oapp.framework.sandbox.cases.persons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "persons")
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id;
    @JsonProperty
    @Column(name = "first_name")
    private String firstName;
    @JsonProperty
    @Column(name = "last_name")
    private String lastName;
}
