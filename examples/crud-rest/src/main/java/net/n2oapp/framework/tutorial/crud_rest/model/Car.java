package net.n2oapp.framework.tutorial.crud_rest.model;

import lombok.Data;

@Data
public class Car {
    private Long id;
    private String name;
    private Long price;
}