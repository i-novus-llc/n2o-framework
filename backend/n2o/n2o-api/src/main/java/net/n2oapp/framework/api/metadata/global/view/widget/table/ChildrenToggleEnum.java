package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Виды отображения дочерних записей таблицы
 */
@RequiredArgsConstructor
@Getter
public enum ChildrenToggleEnum implements N2oEnum {
    COLLAPSE("collapse"),   // свернутый
    EXPAND("expand");       // раскрытый

    private final String id;
}
