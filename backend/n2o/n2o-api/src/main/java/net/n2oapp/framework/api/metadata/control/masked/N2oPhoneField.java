package net.n2oapp.framework.api.metadata.control.masked;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель поля {@code <phone>}
 */
@Getter
@Setter
public class N2oPhoneField extends N2oMaskedField {
    private String[] country;
}
