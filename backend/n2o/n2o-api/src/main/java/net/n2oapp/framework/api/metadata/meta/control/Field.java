package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Клиентская молель поля
 */
@Getter
@Setter
public class Field implements Compiled, PropertiesAware {
    @JsonProperty
    private String id;
    @JsonProperty
    private String src;
    @JsonProperty
    private Boolean required;
    @JsonProperty
    private Boolean visible;
    @JsonProperty
    private Boolean enabled;
    @JsonProperty("dependency")
    private List<ControlDependency> dependencies = new ArrayList<>();
    private Map<String, Object> properties;

    private List<Validation> serverValidations;
    private List<Validation> clientValidations;

    @JsonAnyGetter
    public Map<String, Object> getJsonProperties() {
        return properties;
    }

    public void addDependency(ControlDependency dependency) {
        dependencies.add(dependency);
    }

}
