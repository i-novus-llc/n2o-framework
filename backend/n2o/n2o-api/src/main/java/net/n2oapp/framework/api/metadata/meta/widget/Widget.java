package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;

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
    //need for access transformer
    private String objectId;
    protected T component;
    private Set<String> notCopiedFields;
    @JsonProperty
    private String datasource;
    private String filtersDatasourceId;
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
