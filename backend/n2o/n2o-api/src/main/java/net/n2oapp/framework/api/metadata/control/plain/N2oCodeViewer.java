package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;

/**
 * Компонент для просмотра кода
 */
@Getter
@Setter
public class N2oCodeViewer extends N2oStandardField {
    private String text;
    private CodeLanguageEnum language;
    private ColorThemeEnum theme;
    private Boolean showLineNumbers;
    private Integer startingLineNumber;
    private Boolean hideButtons;

    public enum ColorThemeEnum {
        light, dark
    }
}
