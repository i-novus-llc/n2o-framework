package net.n2oapp.criteria.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Sorting implements Serializable {

    private String field;
    private SortingDirection direction;

    public Sorting(String field, SortingDirection direction) {
        this.field = field;
        this.direction = direction;
    }
}
