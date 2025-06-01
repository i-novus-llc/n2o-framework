package net.n2oapp.framework.api.metadata.meta.control;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Типы валидаций
 */
@RequiredArgsConstructor
@Getter
public enum ValidationTypeEnum implements N2oEnum {
    ENABLED("enabled"),
    VISIBLE("visible"),
    REQUIRED("required"),
    SET_VALUE("setValue"),
    RE_RENDER("reRender"),
    FETCH("fetch"),
    FETCH_VALUE("fetchValue"),
    RESET("reset");

    private final String id;
}
