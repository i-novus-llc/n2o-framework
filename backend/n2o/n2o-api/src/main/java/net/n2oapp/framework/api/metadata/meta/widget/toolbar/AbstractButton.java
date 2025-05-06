package net.n2oapp.framework.api.metadata.meta.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.badge.Badge;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;

import java.util.EnumMap;
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
    private PositionEnum iconPosition;
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
    private Map<ValidationTypeEnum, List<Condition>> conditions = new EnumMap<>(ValidationTypeEnum.class);
    @JsonProperty
    private Badge badge;
    @JsonProperty
    private String datasource;
    @JsonProperty
    private ReduxModelEnum model;
    /**
     * Список источников данных, которые нужно валидировать
     */
    @JsonProperty
    private List<String> validate;
}
