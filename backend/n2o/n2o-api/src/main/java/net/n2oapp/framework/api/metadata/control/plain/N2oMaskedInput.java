package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент ввода текста с маской
 */
@Getter
@Setter
@VisualComponent
public class N2oMaskedInput extends N2oPlainField {
    @VisualAttribute
    private String mask;
    @VisualAttribute
    private String measure;
    @VisualAttribute
    private Boolean clearOnBlur;
}
