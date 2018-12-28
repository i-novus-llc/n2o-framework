package net.n2oapp.tutorial.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String name;
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private Set<Product> products;

}
