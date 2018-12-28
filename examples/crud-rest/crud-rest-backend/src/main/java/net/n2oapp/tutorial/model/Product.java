package net.n2oapp.tutorial.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String name;
    public Long price;
    public String description;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    public Category category;

}
