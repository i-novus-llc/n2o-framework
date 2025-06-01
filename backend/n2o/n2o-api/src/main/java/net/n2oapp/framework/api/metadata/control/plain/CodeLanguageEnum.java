package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Программные языки, синтаксис которых будет использоваться в компонентах
 */
@RequiredArgsConstructor
@Getter
public enum CodeLanguageEnum implements N2oEnum {
    SQL("sql"),
    XML("xml"),
    JAVASCRIPT("javascript"),
    GROOVY("groovy"),
    JAVA("java"),
    HTML("html");

    private final String id;
}
