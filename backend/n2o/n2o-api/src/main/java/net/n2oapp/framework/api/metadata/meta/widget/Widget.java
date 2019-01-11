package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.SrcAware;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.FetchDependency;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;

import java.util.*;

/**
 * Клиентская модель виджета
 */
@Getter
@Setter
public abstract class Widget<T extends WidgetComponent> implements Compiled, SrcAware, PropertiesAware {
    private String id;
    private String route;
    /**
     * Наименование path параметра идентификатора родительского виджета
     */
    private String masterParam;
    /**
     * Ссылка на идентификатор родительского виджета
     */
    private ModelLink masterLink;
    private Boolean opened;
    private String name;
    @JsonProperty
    private String icon;
    private UploadType upload;
    private String objectId;
    private String queryId;
    private List<Filter> filters;
    protected T component;
    private Set<String> notCopiedFields;
    private List<Validation> validations = new ArrayList<>();
    @JsonProperty
    private String src;
    @JsonProperty
    private WidgetDataProvider dataProvider;
    @JsonProperty
    private Toolbar toolbar;
    @JsonProperty
    private Map<String, Action> actions;
    @JsonProperty
    private FetchDependency dependency;
    private Map<String, Object> properties;

    public Widget() {
    }

    public Widget(T component) {
        this.component = component;
    }

    @JsonAnyGetter
    public Map<String, Object> getJsonProperties() {
        return properties;
    }

    public List<SubModelQuery> getSubModelQueriesForField() {
        return Collections.emptyList();//todo
    }

    public List<SubModelQuery> getSubModelQueriesForFilter() {
        return Collections.emptyList();//todo
    }

    public Filter getFilter(String filterId) {
        if (filters == null)
            return null;
        return filters.stream().filter(f -> f.getFilterId().equals(filterId)).findFirst()
                .orElseThrow(() -> new N2oException("Filter " + filterId + " not found"));
    }

}
