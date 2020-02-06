package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.Models;

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
    @JsonProperty("src")
    private String src;
    @JsonProperty("page")
    private PageProperty pageProperty = new PageProperty();
    @JsonProperty
    private PageRoutes routes;
    private CompiledObject object;
    @JsonProperty
    private List<Breadcrumb> breadcrumb;
    @JsonProperty
    private Models models;
    private Map<String, Object> properties;
}
