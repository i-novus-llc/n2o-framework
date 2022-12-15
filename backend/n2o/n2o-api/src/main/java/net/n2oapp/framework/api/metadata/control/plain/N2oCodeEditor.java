package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент редактора кода
 */
@Getter
@Setter
public class N2oCodeEditor extends N2oPlainText {
    private CodeLanguageEnum language;
    private Integer minLines;
    private Integer maxLines;
}
