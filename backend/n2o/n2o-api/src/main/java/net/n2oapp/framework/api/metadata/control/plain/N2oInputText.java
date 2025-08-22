package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ввода текста
 */
@Getter
@Setter
public class N2oInputText extends N2oPlainField {
    private String min;
    private String max;
    private String step;
    private String measure;
    private Integer length;
    private Integer precision;
    private String autocomplete;
}
