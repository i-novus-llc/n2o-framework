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
    private SortingDirectionEnum direction;

    public Sorting(String field, SortingDirectionEnum direction) {
        this.field = field;
        this.direction = direction;
    }
}
