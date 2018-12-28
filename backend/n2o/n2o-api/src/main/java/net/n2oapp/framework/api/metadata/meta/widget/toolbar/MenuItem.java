package net.n2oapp.framework.api.metadata.meta.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Confirm;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Клиентская модель пункта меню
 */
@Getter
@Setter
public class MenuItem implements IdAware, Serializable, PropertiesAware {
    @JsonProperty
    private String id;
    @JsonProperty("title")
    private String label;
    @JsonProperty
    private String icon;
    @JsonProperty
    private Boolean visible;
    @JsonProperty
    private String actionId;
    @JsonProperty
    private Action action;
    @JsonProperty
    private String className;
    @JsonProperty
    private String hint;
    @JsonProperty
    private Map<ValidationType, List<ButtonCondition>> conditions = new HashMap<>();
    @JsonProperty
    private Confirm confirm;
    @JsonProperty
    private Boolean validate;

    private Map<String, Object> properties;

    @JsonAnyGetter
    public Map<String, Object> getJsonProperties() {
        return properties;
    }
}
