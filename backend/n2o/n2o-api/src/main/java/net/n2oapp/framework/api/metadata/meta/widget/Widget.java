package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;

import java.util.List;
import java.util.Set;

/**
 * Клиентская модель виджета
 */
@Getter
@Setter
public abstract class Widget<T extends WidgetComponent> extends Component {
    @JsonProperty
    private String id;
    private String name;
    @JsonProperty
    private String icon;
    private String objectId;
    @Deprecated
    private String filtersDefaultValuesQueryId;
    @Deprecated
    private List<Filter> filters;
    protected T component;
    private Set<String> notCopiedFields;
    @JsonProperty
    private String datasource;
    @JsonProperty
    private Toolbar toolbar;
    @JsonProperty
    private WidgetDependency dependency;
    @JsonProperty
    private Boolean visible;

    public Widget() {
    }

    public Widget(T component) {
        this.component = component;
    }
}
