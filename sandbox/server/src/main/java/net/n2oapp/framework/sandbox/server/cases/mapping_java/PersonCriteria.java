package net.n2oapp.framework.sandbox.server.cases.mapping_java;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Getter
@Setter
public class PersonCriteria implements Pageable {

    private String firstName;

    private int page;
    private int size;

    private List<Sort.Order> orders;

    public PersonCriteria() {
        this.page = 0;
        this.size = 10;
    }

    public PersonCriteria(int page, int size, Sort sort) {
        this.page = page;
        this.size = size;
        this.orders = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(sort.iterator(),
                        Spliterator.ORDERED), false).collect(Collectors.<Sort.Order>toList());
    }

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public long getOffset() {
        return 0;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public Sort getSort() {
        if (orders != null && !orders.isEmpty()) {
            return Sort.by(orders);
        } else
            return Sort.unsorted();
    }

    @Override
    public Pageable next() {
        return new PersonCriteria(this.getPageNumber() + 1, this.getPageSize(), this.getSort());
    }

    @Override
    public Pageable previousOrFirst() {
        return this.hasPrevious() ? this.previous() : this.first();
    }

    public Pageable previous() {
        return this.getPageNumber() == 0 ? this : new PersonCriteria(this.getPageNumber(), this.getPageSize(), this.getSort());
    }

    @Override
    public Pageable first() {
        return new PersonCriteria(0, this.getPageSize(), this.getSort());
    }

    @Override
    public boolean hasPrevious() {
        return this.page > 0;
    }
}
