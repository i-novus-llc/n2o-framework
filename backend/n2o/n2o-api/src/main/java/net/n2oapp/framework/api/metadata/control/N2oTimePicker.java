package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель компонента ввода времени
 */
@Getter
@Setter
public class N2oTimePicker extends N2oControl {
    private String id;
    private String prefix;
    private String[] mode;
    private String dataFormat;
    private String format;
    private String defaultValue;
}
