package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Клиентская модель компонента вывода однострочного текста
 */
@Getter
@Setter
public class OutputText extends Control {
    @JsonProperty
    private String className;
    @JsonProperty
    private String icon;
    @JsonProperty
    private String format;
    @JsonProperty
    private Position iconPosition;
}
