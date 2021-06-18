package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.meta.widget.table.Pagination;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель виджета Плитки
 */
@Getter
@Setter
public class Tiles extends Widget {
    @JsonProperty
    private Integer colsSm;
    @JsonProperty
    private Integer colsMd;
    @JsonProperty
    private Integer colsLg;
    @JsonProperty
    private Integer height;
    @JsonProperty
    private Integer width;
    @JsonProperty
    private Pagination paging;
    @JsonProperty
    private List<Tile> tile;

    @Getter
    @Setter
    public static class Tile extends Component implements IdAware {
        @JsonProperty
        private String id;
        @JsonProperty
        private N2oCell component;
    }
}
