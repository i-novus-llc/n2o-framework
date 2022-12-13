package net.n2oapp.framework.api.metadata.control.plain;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент ввода многострочного текста
 */
@Getter
@Setter
@VisualComponent
public class N2oTextArea extends N2oPlainText {
    @VisualAttribute
    private Integer minRows;
    @VisualAttribute
    private Integer maxRows;
}
