package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ChildrenToggleEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.FilterPositionEnum;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.util.List;

/**
 * Абстрактная реализация клиентской модели таблицы
 */
@Getter
@Setter
public abstract class AbstractTable<T extends TableWidgetComponent> extends Widget<T> {
    @JsonProperty
    private Pagination paging;
    @JsonProperty
    private Filter filter;
    @JsonProperty
    private ChildrenToggleEnum children;
    @JsonProperty
    private Boolean saveSettings;

    @JsonProperty("table")
    @Override
    public T getComponent() {
        return component;
    }

    protected AbstractTable(T component) {
        super(component);
    }

    /**
     * Клиентская модель фильтрации
     */
    @Getter
    @Setter
    public static class Filter implements Compiled {
        @JsonProperty
        private List<FieldSet> filterFieldsets;
        @JsonProperty
        private String filterButtonId;
        @JsonProperty
        private List<String> blackResetList;
        @JsonProperty
        private FilterPositionEnum filterPlace;
        @JsonProperty
        private Boolean fetchOnChange;
        @JsonProperty
        private Boolean fetchOnClear;
        @JsonProperty
        private Boolean fetchOnEnter;
    }
}
