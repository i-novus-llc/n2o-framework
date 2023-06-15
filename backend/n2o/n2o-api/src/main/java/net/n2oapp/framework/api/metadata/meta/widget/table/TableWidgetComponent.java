package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.global.view.widget.table.RowSelectionEnum;
import net.n2oapp.framework.api.metadata.meta.cell.Cell;
import net.n2oapp.framework.api.metadata.meta.widget.Rows;
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
    private String  width;
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
        private List<ColumnHeader> cells;
    }

    @Getter
    @Setter
    public static class TableBody implements Compiled {
        @JsonProperty
        private List<Cell> cells;
        @JsonProperty
        private Rows security;
        @JsonProperty
        private BodyRow row;
    }

    @Getter
    @Setter
    public static class BodyRow implements Compiled {
        @JsonProperty
        private RowClick click;
        @JsonProperty
        private Map<String, String> elementAttributes = new HashMap<>();
    }
}
