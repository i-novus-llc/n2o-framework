package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.ColumnFixedPosition;


/**
 * Заголовки столбцов таблицы
 */
@Getter
@Setter
public class ColumnHeader implements IdAware, Compiled {
    @JsonProperty
    private String id;
    @JsonProperty
    private String label;
    @JsonProperty
    private String src = "TextTableHeader";
    @JsonProperty
    private Boolean sortable;
    @JsonProperty
    private Boolean resizable;
    @JsonProperty
    private Integer width;
    @JsonProperty
    private ColumnFixedPosition fixed;

}
