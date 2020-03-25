package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ввода денежных единиц
 */
@Getter
@Setter
public class N2oInputMoney extends N2oPlainField {
    private String prefix;
    private String suffix;
    private String thousandsSeparator;
    private String decimalSeparator;
    private Integer integerLimit;
    private FractionFormatting fractionFormatting;
}
