package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.N2oComponent;

/**
 * Компонент ввода даты
 */
@Getter
@Setter
@N2oComponent
public class N2oDatePicker extends N2oPlainField {
    @N2oAttribute
    private String dateFormat;
    @N2oAttribute
    private String timeFormat;
    @N2oAttribute
    private String defaultTime;
    @N2oAttribute
    private String max;
    @N2oAttribute
    private String min;
    @N2oAttribute
    private Boolean utc;
}
