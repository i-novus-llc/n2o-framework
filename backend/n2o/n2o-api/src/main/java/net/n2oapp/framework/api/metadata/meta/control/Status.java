package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Клиентская модель компонента отображения статуса
 */
@Getter
@Setter
public class Status extends Field {
    @JsonProperty
    private String color;
    @JsonProperty
    private String text;
    @JsonProperty
    private Position textPosition;
}
