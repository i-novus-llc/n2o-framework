package net.n2oapp.tutorial.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * Адрес
 */
@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @JsonProperty
    @Column(name = "location")
    public String location;
}
