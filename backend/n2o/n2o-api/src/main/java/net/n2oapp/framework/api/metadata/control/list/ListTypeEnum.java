package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Тип выбора в выпадающем списке
 */
@RequiredArgsConstructor
@Getter
public enum ListTypeEnum implements N2oEnum {
    SINGLE("single"),
    MULTI("multi"),
    CHECKBOXES("checkboxes");

    private final String id;
}