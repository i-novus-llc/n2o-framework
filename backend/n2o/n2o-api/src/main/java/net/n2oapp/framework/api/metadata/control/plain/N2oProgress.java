package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент отображения прогресса
 */
@Getter
@Setter
public class N2oProgress extends N2oPlainField {
    private Integer max;
    private String barText;
    private Boolean animated;
    private Boolean striped;
    private String color;
    private String barClass;
}
