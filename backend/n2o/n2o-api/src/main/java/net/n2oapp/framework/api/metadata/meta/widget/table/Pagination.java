package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Клиентская модель для паджинации таблицы.
 */
public class Pagination implements Compiled {
    @JsonProperty("prev")
    private Boolean prev;
    @JsonProperty("next")
    private Boolean next;
    @JsonProperty("size")
    private Integer size;
    private String src;
    private Boolean last;
    private Boolean first;
    private Boolean showCount;
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
