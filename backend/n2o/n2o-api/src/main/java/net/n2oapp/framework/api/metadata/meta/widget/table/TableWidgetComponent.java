package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.RowSelectionEnum;
import net.n2oapp.framework.api.metadata.meta.cell.Cell;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Клиенсткая модель компонента таблицы
 */
@Getter
@Setter
public class TableWidgetComponent extends WidgetComponent {
    @JsonProperty
    private String width;
    @JsonProperty
    private String height;
    @JsonProperty
    private Boolean textWrap;
    @JsonProperty
    private String tableSize;
    @JsonProperty
    private TableHeader header = new TableHeader();
    @JsonProperty
    private TableBody body = new TableBody();
    @JsonProperty
    private RowSelectionEnum rowSelection;
    @JsonProperty
    private Boolean autoSelect;

    @Getter
    @Setter
    public static class TableHeader implements Compiled {
        @JsonProperty
        private List<AbstractColumn> cells;
    }

    @Getter
    @Setter
    public static class TableBody implements Compiled {
        @JsonProperty
        private List<Cell> cells;
        @JsonProperty
        private BodyRow row;
    }

    @Getter
    @Setter
    public static class BodyRow implements Compiled, JsonPropertiesAware {
        @JsonProperty
        private String src;
        @JsonProperty
        private RowClick click;
        @JsonProperty
        private RowOverlay overlay;
        @JsonProperty
        private Map<String, Object> elementAttributes = new HashMap<>();
        private Map<String, Object> properties;
    }
}
