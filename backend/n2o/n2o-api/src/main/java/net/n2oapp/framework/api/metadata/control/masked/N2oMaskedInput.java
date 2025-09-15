package net.n2oapp.framework.api.metadata.control.masked;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель поля {@code <masked-input>}
 */
@Getter
@Setter
public class N2oMaskedInput extends N2oMaskedField {
    private String mask;
    private String measure;
    private Boolean clearOnBlur;
    private String autocomplete;
}
