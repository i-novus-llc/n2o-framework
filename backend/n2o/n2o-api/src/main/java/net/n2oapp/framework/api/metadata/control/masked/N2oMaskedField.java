package net.n2oapp.framework.api.metadata.control.masked;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.plain.N2oPlainField;

/**
 * Исходная модель поля ввода с маской
 */
@Getter
@Setter
public class N2oMaskedField extends N2oPlainField {
    private String invalidText;
}
