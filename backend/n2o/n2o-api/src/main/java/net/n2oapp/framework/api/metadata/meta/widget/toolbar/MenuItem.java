package net.n2oapp.framework.api.metadata.meta.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Клиентская модель пункта меню
 */
@Getter
@Setter
public class MenuItem extends Component implements IdAware {
    @JsonProperty
    private String id;
    @JsonProperty
    private String label;
    @JsonProperty
    private String icon;
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
    private Map<ValidationTypeEnum, List<Condition>> conditions = new EnumMap<>(ValidationTypeEnum.class);
    @JsonProperty
    private Boolean validate;
    @JsonProperty
    private String validatedWidgetId;
}
