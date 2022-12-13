package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.control.plain.N2oPlainField;

/**
 * Исходная модель компонента выбора числа из диапазона
 */
@Getter
@Setter
@VisualComponent
public class N2oNumberPicker extends N2oPlainField {
    @VisualAttribute
    private Integer max;
    @VisualAttribute
    private Integer min;
    @VisualAttribute
    private Integer step;
}
