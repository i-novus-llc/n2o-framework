package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент ввода пароля
 */
@Getter
@Setter
@VisualComponent
public class N2oPassword extends N2oPlainField {
    @VisualAttribute
    private Integer length;
    @VisualAttribute
    private Boolean eye;
}
