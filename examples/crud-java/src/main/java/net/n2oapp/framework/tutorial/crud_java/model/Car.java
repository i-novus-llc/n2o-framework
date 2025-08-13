package net.n2oapp.framework.tutorial.crud_java.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class Car {
    private Long id;
    private String name;
    private Long price;
}