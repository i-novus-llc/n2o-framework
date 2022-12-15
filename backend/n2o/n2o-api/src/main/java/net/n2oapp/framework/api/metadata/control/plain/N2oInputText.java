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
    @N2oAttribute
    private Integer length;
    @N2oAttribute
    private String max;
    @N2oAttribute
    private String min;
    @N2oAttribute
    private String step;
    @N2oAttribute
    private String measure;
    @N2oAttribute
    private Integer precision;
}
