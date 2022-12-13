package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент текста
 */
@Getter
@Setter
@VisualComponent
public class N2oText extends N2oField {
    @VisualAttribute
    private String text;
    @VisualAttribute
    private String format;
}
