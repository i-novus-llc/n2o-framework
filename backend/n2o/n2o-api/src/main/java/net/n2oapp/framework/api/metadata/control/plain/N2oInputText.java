package net.n2oapp.framework.api.metadata.control.plain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ввода текста
 */
@Getter
@Setter
public class N2oInputText extends N2oPlainField {

    public N2oInputText(String id) {
        setId(id);
    }

    public N2oInputText() {
    }

    private Integer length;
    private String max;
    private String min;
    private String step;

}
