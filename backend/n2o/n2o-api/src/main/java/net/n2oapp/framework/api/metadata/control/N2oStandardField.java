package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;


/**
 * Абстрактная реализация контрола
 */
@Getter
@Setter
public abstract class N2oStandardField extends N2oField {
    @N2oAttribute("Подсказка для ввода")
    private String placeholder;
    private Submit submit;
}
