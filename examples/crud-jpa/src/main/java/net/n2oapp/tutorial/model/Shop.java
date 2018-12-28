package net.n2oapp.tutorial.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Магазин
 */
@Entity
@Data
public class Shop {
    @JsonProperty
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    public Address address;

    @JsonProperty
    @Column
    public String name;

    @JsonProperty
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_shop", joinColumns = {@JoinColumn(name = "shop_id")}, inverseJoinColumns = {@JoinColumn(name = "product_id")})
    public List<Product> products;

    public Shop() {
    }

    public Shop(Long id) {
        this.id = id;
    }

}
