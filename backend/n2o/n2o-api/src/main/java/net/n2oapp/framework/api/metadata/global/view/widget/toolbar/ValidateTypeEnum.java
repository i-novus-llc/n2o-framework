package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Тип валидации виджетов страницы
 */
@RequiredArgsConstructor
@Getter
public enum ValidateTypeEnum implements N2oEnum {
    PAGE("page"),
    WIDGET("widget"),
    NONE("none");

    private final String id;
}