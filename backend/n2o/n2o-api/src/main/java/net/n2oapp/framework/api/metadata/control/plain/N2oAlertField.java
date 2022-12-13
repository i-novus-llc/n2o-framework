package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент вывода уведомления
 */
@Getter
@Setter
@VisualComponent
public class N2oAlertField extends N2oPlainField {
    @VisualAttribute
    private String title;
    @VisualAttribute
    private String text;
    @VisualAttribute
    private String href;
    @VisualAttribute
    private String color;
    @VisualAttribute
    private Boolean closeButton;
}
