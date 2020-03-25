package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента ввода денежных единиц
 */
@Getter
@Setter
public class InputMoney extends Control {
    @JsonProperty
    private String prefix;
    @JsonProperty
    private String suffix;
    @JsonProperty
    private String thousandsSeparatorSymbol;
    @JsonProperty
    private String decimalSymbol;
    @JsonProperty
    private Integer integerLimit;
    @JsonProperty
    private Boolean requireDecimal;
    @JsonProperty
    private Boolean allowDecimal;
}
