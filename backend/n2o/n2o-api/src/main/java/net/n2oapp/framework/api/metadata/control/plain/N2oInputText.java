package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент ввода текста
 */
@Getter
@Setter
@VisualComponent
public class N2oInputText extends N2oPlainField {
    @VisualAttribute
    private Integer length;
    @VisualAttribute
    private String max;
    @VisualAttribute
    private String min;
    @VisualAttribute
    private String step;
    @VisualAttribute
    private String measure;
    @VisualAttribute
    private Integer precision;

    public N2oInputText() {
    }

    public N2oInputText(String id) {
        setId(id);
    }
}
