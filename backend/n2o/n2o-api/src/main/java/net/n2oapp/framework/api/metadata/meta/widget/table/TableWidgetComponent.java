package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.RowSelectionEnum;
import net.n2oapp.framework.api.metadata.meta.cell.Cell;
import net.n2oapp.framework.api.metadata.meta.widget.Rows;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetComponent;

import java.util.HashMap;
import java.util.List;

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
    private List<Cell> cells;
    @JsonProperty
    private List<ColumnHeader> headers;
    @JsonProperty
    private TableBody body = new TableBody();
    @JsonProperty
    private RowSelectionEnum rowSelection;
    @JsonProperty
    private Boolean autoSelect;

    public static class TableHeader {

    }

    @Getter
    @Setter
    public static class TableBody {
        @JsonProperty
        private Rows security;
        @JsonProperty
        private BodyRow row;
    }

    @Getter
    @Setter
    public static class BodyRow {
        @JsonProperty
        private Boolean hasFocus = true;
        @JsonProperty
        private Boolean hasSelect = true;
        @JsonProperty
        private RowClick click;
        @JsonProperty
        private HashMap<String, String> elementAttributes = new HashMap<>();
    }
}
