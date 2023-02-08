package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Confirm;
import net.n2oapp.framework.api.metadata.meta.badge.Badge;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;

import java.util.HashMap;
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
    private Confirm confirm;
    @JsonProperty
    private Badge badge;
    /**
     * Список источников данных, которые нужно валидировать
     */
    @JsonProperty
    private List<String> validate;

    @JsonProperty
    private Map<ValidationType, List<Condition>> conditions = new HashMap<>();
}
