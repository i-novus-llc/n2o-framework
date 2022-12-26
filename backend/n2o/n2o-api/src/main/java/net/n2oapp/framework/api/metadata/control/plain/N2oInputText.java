package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.N2oComponent;

/**
 * Компонент ввода текста
 */
@Getter
@Setter
@N2oComponent
public class N2oInputText extends N2oPlainField {
    @N2oAttribute("Минимальное значение")
    private String min;
    @N2oAttribute("Максимальное значение")
    private String max;
    @N2oAttribute("Шаг приращения")
    private String step;
    @N2oAttribute("Единица измерения")
    private String measure;
    @N2oAttribute("Максимальное количество символов для ввода")
    private Integer length;
    @N2oAttribute("Максимальная длина дробной части")
    private Integer precision;
}
