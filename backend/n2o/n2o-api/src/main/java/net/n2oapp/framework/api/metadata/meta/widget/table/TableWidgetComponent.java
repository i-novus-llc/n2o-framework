package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetComponent;

import java.util.List;
import java.util.Map;

/**
 * Клиенсткая модель компонента таблицы
 */
@Getter
@Setter
public class TableWidgetComponent extends WidgetComponent {
    @JsonProperty("className")
    private String className;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("rowColor")
    private String rowColor;
    @JsonProperty("autoFocus")
    private Boolean autoFocus = true;
    @JsonProperty("hasFocus")
    private Boolean hasFocus = true;
    @JsonProperty("cells")
    private List<N2oCell> cells;
    @JsonProperty("headers")
    private List<ColumnHeader> headers;
    @JsonProperty("sorting")
    private Map<String, String> sorting;
    @JsonProperty("hasSelect")
    private Boolean hasSelect = true;

}
