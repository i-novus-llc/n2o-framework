package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.meta.cell.Cell;
import net.n2oapp.framework.api.metadata.meta.widget.table.Pagination;

import java.util.List;

/**
 * Клиентская модель виджета карточки
 */
@Getter
@Setter
public class Cards extends Widget {
    @JsonProperty
    private PositionEnum verticalAlign;
    @JsonProperty
    private String height;
    @JsonProperty
    private Pagination paging;
    @JsonProperty("cards")
    private List<Card> cardList;

    @Getter
    @Setter
    public static class Card implements IdAware, Compiled {
        @JsonProperty
        private String id;
        @JsonProperty("col")
        private Integer size;
        @JsonProperty
        private List<Block> content;
    }

    @Getter
    @Setter
    public static class Block extends Component implements IdAware {
        @JsonProperty
        private String id;
        @JsonProperty
        private Cell component;
    }

    public enum PositionEnum {
        center, top, bottom
    }
}
