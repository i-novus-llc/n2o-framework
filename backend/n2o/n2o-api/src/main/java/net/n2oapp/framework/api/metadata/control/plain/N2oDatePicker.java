package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ввода даты
 */
@Getter
@Setter
public class N2oDatePicker extends N2oPlainField {
    private String dateFormat;
    private String timeFormat;
    private String defaultTime;
    private String max;
    private String min;
    private Boolean utc;

}
