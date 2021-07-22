package net.n2oapp.criteria.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Criteria implements Serializable {

    private List<Sorting> sortings;
    private int page = 1;
    private int size = 15;
    private Integer count;//if you know the total number of records

    public Criteria() {
    }

    public Criteria(Criteria criteria) {
        this.sortings = criteria.sortings;
        this.page = criteria.page;
        this.size = criteria.size;
        this.count = criteria.count;
    }

    public int getFirst() {
        return (page - 1) * size;
    }

    public Sorting getSorting() {
        return sortings != null && !sortings.isEmpty() ? sortings.get(0) : null;
    }

    public void setSorting(Sorting sorting) {
        if (sortings == null) {
            sortings = new ArrayList<>();
        }
        sortings.add(0, sorting);
    }
}
