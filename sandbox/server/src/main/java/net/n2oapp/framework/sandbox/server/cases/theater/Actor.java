package net.n2oapp.framework.sandbox.server.cases.theater;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

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
