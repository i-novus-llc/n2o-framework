package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;

import java.util.ArrayList;
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
    /**
     * Наименование path параметра идентификатора родительского виджета
     */
    @Deprecated
    private String masterParam;
    private String name;
    @JsonProperty
    private String icon;
    @Deprecated
    private UploadType upload;
    @Deprecated
    private String objectId;
    @Deprecated
    private String queryId;
    @Deprecated
    private String filtersDefaultValuesQueryId;
    @Deprecated
    private List<Filter> filters;
    protected T component;
    private Set<String> notCopiedFields;
    @Deprecated
    private List<Validation> validations = new ArrayList<>();
    @JsonProperty
    @Deprecated
    private ClientDataProvider dataProvider;
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

    public Filter getFilter(String filterId) {
        if (filters == null)
            return null;
        return filters.stream().filter(f -> f.getFilterId().equals(filterId)).findFirst()
                .orElseThrow(() -> new N2oException("Filter " + filterId + " not found"));
    }

    public boolean containsFilter(String filterId) {
        return filters != null && filters.stream().anyMatch(f -> f.getFilterId().equals(filterId));
    }
}
