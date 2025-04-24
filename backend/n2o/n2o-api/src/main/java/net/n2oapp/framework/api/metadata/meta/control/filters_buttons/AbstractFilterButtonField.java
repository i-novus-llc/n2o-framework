package net.n2oapp.framework.api.metadata.meta.control.filters_buttons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.badge.Badge;
import net.n2oapp.framework.api.metadata.meta.control.Field;

/**
 * Абстрактное поле для кнопки-фильтра
 */
@Getter
@Setter
public abstract class AbstractFilterButtonField extends Field {

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
    private String datasource;
}
