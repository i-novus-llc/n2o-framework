package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Исходная модель настраиваемого поля
 */
@Getter
@Setter
@VisualComponent
public class N2oCustomField extends N2oField {
    @VisualAttribute
    private N2oComponent[] controls;
}
