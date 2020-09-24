package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Клиентская модель для паджинации таблицы.
 */
public class Pagination implements Compiled {
    @JsonProperty
    private Boolean prev;
    @JsonProperty
    private Boolean next;
    @JsonProperty
    private Integer size;
    @JsonProperty
    private String src;
    @JsonProperty
    private Boolean last;
    @JsonProperty
    private Boolean first;
    @JsonProperty
    private Boolean showCount;
    @JsonProperty
    private Boolean hideSinglePage;

    public Boolean getPrev() {
        return prev;
    }

    public void setPrev(Boolean prev) {
        this.prev = prev;
    }

    public Boolean getNext() {
        return next;
    }

    public void setNext(Boolean next) {
        this.next = next;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getShowCount() {
        return showCount;
    }

    public void setShowCount(Boolean showCount) {
        this.showCount = showCount;
    }

    public Boolean getHideSinglePage() {
        return hideSinglePage;
    }

    public void setHideSinglePage(Boolean hideSinglePage) {
        this.hideSinglePage = hideSinglePage;
    }
}
