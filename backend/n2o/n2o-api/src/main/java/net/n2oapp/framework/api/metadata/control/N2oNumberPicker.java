package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель компонента ввода чисел "плюс-минус"
 */
@Getter
@Setter
public class N2oNumberPicker extends N2oField {

    private Integer max;
    private Integer min;
    private Integer step;

}
