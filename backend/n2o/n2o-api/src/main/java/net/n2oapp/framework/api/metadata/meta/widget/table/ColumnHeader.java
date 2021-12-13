package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.ColumnFixedPosition;
import net.n2oapp.framework.api.metadata.meta.control.Control;
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
    private Boolean sortable;
    @JsonProperty
    private Boolean resizable;
    @JsonProperty
    private String width;
    @JsonProperty
    private Object visible;
    @JsonProperty
    private ColumnFixedPosition fixed;
    @JsonProperty
    private Boolean filterable;
    @JsonProperty
    private Control filterControl;
    @JsonProperty
    private Boolean multiHeader;
    @JsonProperty("className")
    private String cssClass;
    @JsonProperty
    private Map<String, String> style;
    @JsonProperty
    private List<ColumnHeader> children;
    @JsonProperty
    private Map<ValidationType, List<Condition>> conditions = new HashMap<>();
    private Map<String, Object> properties;
}
