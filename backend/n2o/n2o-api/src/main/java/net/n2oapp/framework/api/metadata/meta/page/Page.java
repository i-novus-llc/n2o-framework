package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.datasource.Datasource;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель страницы n2o
 */
@Getter
@Setter
public class Page extends Component {
    @JsonProperty
    private String id;
    @JsonProperty("page")
    private PageProperty pageProperty = new PageProperty();
    @JsonProperty
    private PageRoutes routes;
    @JsonProperty
    private Toolbar toolbar;
    @Deprecated
    private CompiledObject object;
    @JsonProperty
    private List<Breadcrumb> breadcrumb;
    @JsonProperty
    private Models models;
    @JsonProperty
    private Map<String, Datasource> datasources;
}
