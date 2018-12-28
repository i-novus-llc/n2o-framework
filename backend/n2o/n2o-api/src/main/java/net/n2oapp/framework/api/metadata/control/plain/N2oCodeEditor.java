package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ввода редактирования кода
 */
@Getter
@Setter
public class N2oCodeEditor extends N2oText {
    private Language language;


    public enum Language {
        sql,
        xml,
        javascript,
        groovy,
        java
    }
}
