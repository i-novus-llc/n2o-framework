package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Генерация стандартных действий
 */
@RequiredArgsConstructor
@Getter
public enum GenerateTypeEnum implements N2oEnum {
    CRUD("crud"),
    SAVE_AND_CLOSE("saveAndClose"),
    CLOSE("close"),
    SUBMIT("submit"),
    TABLE_SETTINGS("tableSettings"),
    REFRESH("refresh"),
    RESIZE("resize"),
    EXPORT("export"),
    COLUMNS("columns"),
    FILTERS("filters");

    private final String id;
}
