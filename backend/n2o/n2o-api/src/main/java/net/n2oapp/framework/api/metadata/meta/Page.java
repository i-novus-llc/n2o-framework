package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель страницы n2o
 */
@Getter
@Setter
public class Page implements Compiled, JsonPropertiesAware {
    @JsonProperty
    private String id;
    @JsonProperty("layout")
    private Layout layout;
    @JsonProperty("page")
    private PageProperty pageProperty = new PageProperty();
    @JsonProperty
    private PageRoutes routes;
    @JsonProperty
    private Map<String, Widget> widgets;
    @JsonProperty
    private Toolbar toolbar;
    private CompiledObject object;
    @JsonProperty
    private List<Breadcrumb> breadcrumb;
    @JsonProperty
    private Map<String, Action> actions;
    @JsonProperty
    private Models models;
    private Map<String, Object> properties;
}
