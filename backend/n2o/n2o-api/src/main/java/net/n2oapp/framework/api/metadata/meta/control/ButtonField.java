package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.badge.Badge;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Клиентская модель компонента ButtonField
 */
@Getter
@Setter
public class ButtonField extends ActionField {
    @JsonProperty
    private String color;
    @JsonProperty
    private String icon;
    @JsonProperty
    private String hint;
    @JsonProperty
    private String hintPosition;

    @JsonProperty
    private Badge badge;
    @JsonProperty
    private Boolean rounded;
    @JsonProperty
    private String datasource;
    /**
     * Список источников данных, которые нужно валидировать
     */
    @JsonProperty
    private List<String> validate;

    @JsonProperty
    private Map<ValidationTypeEnum, List<Condition>> conditions = new EnumMap<>(ValidationTypeEnum.class);
}