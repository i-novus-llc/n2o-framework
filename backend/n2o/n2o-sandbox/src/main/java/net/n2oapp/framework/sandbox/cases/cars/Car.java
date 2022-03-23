package net.n2oapp.framework.sandbox.cases.cars;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "cars")
@Data
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id;
    @JsonProperty
    @Column
    private String name;
    @JsonProperty
    @Column
    private Long price;
}
