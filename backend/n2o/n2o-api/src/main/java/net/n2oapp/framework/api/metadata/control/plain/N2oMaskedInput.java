package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ввода текста с маской
 */
@Getter
@Setter
public class N2oMaskedInput extends N2oPlainField {
    private String mask;
    private String measure;
    private Boolean clearOnBlur;
}
