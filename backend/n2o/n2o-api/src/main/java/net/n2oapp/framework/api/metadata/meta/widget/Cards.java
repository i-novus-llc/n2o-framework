package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.meta.widget.table.Pagination;

/**
 * Клиентская модель виджета карточки
 */
@Getter
@Setter
public class Cards extends Widget {

    @JsonProperty
    private Position align;
    @JsonProperty
    private Pagination paging;
    @JsonProperty
    private Card[] cards;

    @Getter
    @Setter
    public static class Card implements IdAware, Compiled {
        @JsonProperty
        private String id;
        @JsonProperty("col")
        private Integer size;
        @JsonProperty
        private N2oCell[] content;
    }

    public enum Position {
        center, top, bottom
    }

}
