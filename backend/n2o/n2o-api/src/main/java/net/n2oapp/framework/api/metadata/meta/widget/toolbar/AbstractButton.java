package net.n2oapp.framework.api.metadata.meta.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Confirm;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.badge.Badge;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Абстрактная модель кнопки в меню
 */
@Getter
@Setter
public abstract class AbstractButton extends Component implements IdAware {
    @JsonProperty
    private String id;
    @JsonProperty
    private String label;
    @JsonProperty
    private String icon;
    @JsonProperty
    private String color;
    @JsonProperty
    private Boolean rounded;
    @JsonProperty
    private Object visible;
    @JsonProperty
    private Object enabled;
    @JsonProperty
    private Action action;
    @JsonProperty
    private String hint;
    @JsonProperty
    private String hintPosition;
    @JsonProperty
    private Map<ValidationType, List<Condition>> conditions = new HashMap<>();
    @JsonProperty
    private Confirm confirm;
    @JsonProperty
    private Badge badge;
    @JsonProperty
    private String datasource;
    /**
     * Список источников данных, которые нужно валидировать
     */
    @JsonProperty
    private List<String> validate;

    @JsonAnyGetter
    public Map<String, Object> getJsonProperties() {
        return getProperties();
    }
}
