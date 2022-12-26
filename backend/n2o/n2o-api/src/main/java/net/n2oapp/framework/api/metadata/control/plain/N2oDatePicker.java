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
    @N2oAttribute("Формат даты")
    private String dateFormat;
    @N2oAttribute("Формат времени")
    private String timeFormat;
    @N2oAttribute("Время по умолчанию")
    private String defaultTime;
    @N2oAttribute("Максимальная дата")
    private String max;
    @N2oAttribute("Минимальная дата")
    private String min;
    @N2oAttribute("Время в UTC")
    private Boolean utc;
}
