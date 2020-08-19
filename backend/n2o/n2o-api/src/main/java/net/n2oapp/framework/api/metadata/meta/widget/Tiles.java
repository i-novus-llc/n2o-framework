package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;

import java.io.Serializable;
import java.util.List;

/**
 * Клиентская модель виджета плитки
 */
@Getter
@Setter
public class Tiles extends Widget {

    @JsonProperty
    private String id;
    @JsonProperty
    private int colsSm;
    @JsonProperty
    private int colsMd;
    @JsonProperty
    private int colsLg;
    @JsonProperty
    private String src;
    @JsonProperty
    private List<Tile> tile;

    @Getter
    @Setter
    public static class Tile implements Serializable {

        @JsonProperty
        private String id;
        @JsonProperty
        private String className;
        @JsonProperty
        private String style;
        @JsonProperty
        private String src;
        @JsonProperty
        private N2oCell component;

    }

}
