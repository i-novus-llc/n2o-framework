package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель поля для вывода оповещения
 */
@Getter
@Setter
public class Alert extends Control {
    @JsonProperty
    private String text;
    @JsonProperty
    private String header;
    @JsonProperty
    private String footer;
    @JsonProperty
    private String color;
    @JsonProperty
    private Boolean fade;
    @JsonProperty
    private String tag;
}
