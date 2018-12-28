package net.n2oapp.framework.api.metadata.control.interval;

import lombok.Getter;
import lombok.Setter;
/**
 * Компонент ввода чисел
 */
@Getter
@Setter
public class N2oInputInterval extends N2oIntervalField {
    private Integer max;
    private Integer min;
    private String step;
}
