package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент ввода денежных единиц
 */
@Getter
@Setter
@VisualComponent
public class N2oInputMoney extends N2oPlainField {
    @VisualAttribute
    private String prefix;
    @VisualAttribute
    private String suffix;
    @VisualAttribute
    private String thousandsSeparator;
    @VisualAttribute
    private String decimalSeparator;
    @VisualAttribute
    private Integer integerLimit;
    @VisualAttribute
    private FractionFormatting fractionFormatting;
}
