package net.n2oapp.tutorial.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Товар
 */
@Entity
@Data
public class Product {

    @JsonProperty
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @JsonProperty
    @Column
    public String name;

    @JsonProperty
    @Column
    public String description;

    @JsonProperty
    @Column
    public Integer quantity;

    @JsonProperty
    @Column
    public Long price;

    @JsonProperty
    @ManyToOne
    @JoinColumn(name = "category_id")
    public Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_shop", joinColumns = {@JoinColumn(name = "product_id")}, inverseJoinColumns = {@JoinColumn(name = "shop_id")})
    public List<Shop> shops;

    public Product() {
    }

    public Product(Long categoryId, Integer quantity, Long price) {
        this.quantity = quantity;
        this.price = price;
        this.category = new Category(categoryId);
    }
}
