package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент редактора кода
 */
@Getter
@Setter
@VisualComponent
public class N2oCodeEditor extends N2oPlainText {
    @VisualAttribute
    private CodeLanguageEnum language;
    @VisualAttribute
    private Integer minLines;
    @VisualAttribute
    private Integer maxLines;
}
