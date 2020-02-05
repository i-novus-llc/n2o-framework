package net.n2oapp.framework.api.metadata.global.view.widget.table;

import net.n2oapp.framework.api.metadata.Source;

/**
 * Исходная модель для паджинации таблицы.
 */
public class N2oPagination implements Source {
    private String src;
    private Boolean prev;
    private Boolean next;
    private Boolean last;
    private Boolean first;
    private Boolean showCount;
    private Boolean hideSinglePage;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

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
