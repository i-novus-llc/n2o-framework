package net.n2oapp.framework.api.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Модель на клиенте
 */
@RequiredArgsConstructor
@Getter
public enum ReduxModelEnum implements N2oEnum {
    RESOLVE("resolve"),
    FILTER("filter"),
    SELECTED("selected"),
    EDIT("edit"),
    MULTI("multi"),
    DATASOURCE("datasource");

    private final String id;
}