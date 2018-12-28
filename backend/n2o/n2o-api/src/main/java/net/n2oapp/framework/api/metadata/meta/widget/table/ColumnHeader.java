package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.global.aware.IdAware;

import java.util.Set;

/**
 * Заголовки столбцов таблицы
 */
public class ColumnHeader implements IdAware, Compiled {
    @JsonProperty
    private String id;
    @JsonProperty
    private String label;
    @JsonProperty
    private String src = "TextTableHeader";
    @JsonProperty
    private Boolean sortable;

    public Boolean getSortable() {
        return sortable;
    }

    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
