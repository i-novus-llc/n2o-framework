package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.RowSelectionEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.meta.widget.Rows;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetComponent;

import java.util.List;
import java.util.Map;

/**
 * Клиенсткая модель компонента таблицы
 */
@Getter
@Setter
public class TableWidgetComponent extends WidgetComponent {
    @JsonProperty
    private Integer size;
    @JsonProperty
    private String rowClass;
    @JsonProperty
    private Boolean hasFocus = true;
    @JsonProperty
    private Boolean hasSelect = true;
    @JsonProperty
    private String  width;
    @JsonProperty
    private String height;
    @JsonProperty
    private Boolean textWrap;
    @JsonProperty
    private String tableSize;
    @JsonProperty
    private List<N2oCell> cells;
    @JsonProperty
    private List<ColumnHeader> headers;
    @JsonProperty
    private Map<String, String> sorting;
    @JsonProperty
    private RowClick rowClick;
    @JsonProperty
    private Rows rows;
    @JsonProperty
    private Boolean autoCheckboxOnSelect;
    @JsonProperty
    private RowSelectionEnum rowSelection;
    @JsonProperty
    private Boolean autoSelect;
}
