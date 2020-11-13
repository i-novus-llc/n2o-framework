package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.plain.N2oPlainField;

/**
 * Исходная модель компонента выбора числа из диапазона
 */
@Getter
@Setter
public class N2oNumberPicker extends N2oPlainField {
    private Integer max;
    private Integer min;
    private Integer step;
}
