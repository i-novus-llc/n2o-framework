package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.plain.N2oPlainField;

/**
 * Исходная модель компонента ввода времени
 */
@Getter
@Setter
public class N2oTimePicker extends N2oPlainField {
    private String prefix;
    private String[] mode;
    private String timeFormat;
    private String format;
}
