package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Представление компонента
 */
@RequiredArgsConstructor
@Getter
public enum LayoutEnum implements N2oEnum {
    BORDERED("bordered"),
    FLAT("flat"),
    SEPARATED("separated"),
    BORDERED_ROUNDED("bordered-rounded"),
    FLAT_ROUNDED("flat-rounded"),
    SEPARATED_ROUNDED("separated-rounded");

    private final String id;
}