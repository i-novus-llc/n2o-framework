package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.control.plain.N2oPlainField;

/**
 * Исходная модель компонента ввода времени
 */
@Getter
@Setter
@VisualComponent
public class N2oTimePicker extends N2oPlainField {
    @VisualAttribute
    private String prefix;
    @VisualAttribute
    private String[] mode;
    @VisualAttribute
    private String timeFormat;
    @VisualAttribute
    private String format;
}
