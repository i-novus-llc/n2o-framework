package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.ColumnFixedPosition;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Заголовки столбцов таблицы
 */
@Getter
@Setter
public class ColumnHeader implements IdAware, Compiled, JsonPropertiesAware {
    @JsonProperty
    private String id;
    @JsonProperty
    private String label;
    @JsonProperty
    private String icon;
    @JsonProperty
    private String src;
    @JsonProperty
    private String sortingParam;
    @JsonProperty
    private Boolean resizable;
    @JsonProperty
    private Object visible;
    @JsonProperty
    private ColumnFixedPosition fixed;
    @JsonProperty
    private Boolean filterable;
    @JsonProperty
    private StandardField filterField;
    @JsonProperty
    private Boolean multiHeader;
    @JsonProperty
    private List<ColumnHeader> children;
    @JsonProperty
    private Map<ValidationType, List<Condition>> conditions = new HashMap<>();
    @JsonProperty
    private Map<String, Object> elementAttributes = new HashMap<>();
    private Map<String, Object> properties;
}
