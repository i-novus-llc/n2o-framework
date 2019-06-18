package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * Клиентская модель поля для ввода текста
 */
@Getter
@Setter
public class Password extends Control {
    @JsonProperty
    private Integer length;
    @JsonProperty
    private String placeholder;
    @JsonProperty("showPasswordBtn")
    private Boolean eye;
}
