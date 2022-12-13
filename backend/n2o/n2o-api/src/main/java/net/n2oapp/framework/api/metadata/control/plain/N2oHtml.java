package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент вывода html
 */
@Getter
@Setter
@VisualComponent
public class N2oHtml extends N2oPlainField {
    @VisualAttribute
    private String html;
}
