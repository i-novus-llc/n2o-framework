package net.n2oapp.criteria.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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

    public List<Sorting> getSortings() {
        return sortings;
    }

    public void setSortings(List<Sorting> sortings) {
        this.sortings = sortings;
    }

}
