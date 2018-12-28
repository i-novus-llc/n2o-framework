package net.n2oapp.tutorial.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Категория
 */
@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    public String name;

    public Category() {
    }

    public Category(Long id) {
        this.id = id;
    }
}
