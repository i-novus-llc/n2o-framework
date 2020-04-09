package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента отображения прогресса
 */
@Getter
@Setter
public class Progress extends Field {
    @JsonProperty
    private Integer max;
    @JsonProperty
    private String barText;
    @JsonProperty
    private Boolean animated;
    @JsonProperty
    private Boolean striped;
    @JsonProperty
    private String color;
    @JsonProperty("barClassName")
    private String barClass;
}
