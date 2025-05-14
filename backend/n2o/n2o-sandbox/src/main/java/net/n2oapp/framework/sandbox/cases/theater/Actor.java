package net.n2oapp.framework.sandbox.cases.theater;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "actors")
@Data
public class Actor {
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
    @JsonProperty
    @Column(name = "country")
    private String country;
}
