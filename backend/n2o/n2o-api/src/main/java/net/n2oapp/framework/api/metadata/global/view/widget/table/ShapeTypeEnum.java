package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Форма square/circle/rounded
 */
@RequiredArgsConstructor
@Getter
public enum ShapeTypeEnum implements N2oEnum {
    SQUARE("square"),
    CIRCLE("circle"),
    ROUNDED("rounded");

    private final String id;
}