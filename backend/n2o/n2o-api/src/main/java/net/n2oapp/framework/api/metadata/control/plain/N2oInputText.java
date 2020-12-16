package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ввода текста
 */
@Getter
@Setter
public class N2oInputText extends N2oPlainField {
    private Integer length;
    private String max;
    private String min;
    private String step;
    private String measure;
    private Integer precision;

    public N2oInputText() {
    }

    public N2oInputText(String id) {
        setId(id);
    }
}
