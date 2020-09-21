package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;

import java.util.List;

/**
 * Клиентская модель виджета плитки
 */
@Getter
@Setter
public class Tiles extends Widget {

    @JsonProperty
    private int colsSm;
    @JsonProperty
    private int colsMd;
    @JsonProperty
    private int colsLg;
    @JsonProperty
    private List<Tile> tile;

    @Getter
    @Setter
    public static class Tile extends Component {

        @JsonProperty
        private String id;
        @JsonProperty
        private N2oCell component;

    }

}
