package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель настраиваемого поля
 */
@Getter
@Setter
public class N2oCustomField extends N2oField {
    private N2oComponent[] controls;
}
