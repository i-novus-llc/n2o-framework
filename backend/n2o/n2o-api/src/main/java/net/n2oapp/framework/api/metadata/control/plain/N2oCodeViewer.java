package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;

/**
 * Компонент для просмотра кода
 */
@Getter
@Setter
@VisualComponent
public class N2oCodeViewer extends N2oStandardField {
    @VisualAttribute
    private String text;
    @VisualAttribute
    private CodeLanguageEnum language;
    @VisualAttribute
    private ColorTheme theme;
    @VisualAttribute
    private Boolean showLineNumbers;
    @VisualAttribute
    private Integer startingLineNumber;
    @VisualAttribute
    private Boolean hideButtons;
    @VisualAttribute
    private Boolean hideOverflow;

    public enum ColorTheme {
        light, dark
    }
}
