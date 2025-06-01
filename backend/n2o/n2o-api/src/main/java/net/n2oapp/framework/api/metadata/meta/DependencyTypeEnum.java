package net.n2oapp.framework.api.metadata.meta;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Тип зависимости
 */

@RequiredArgsConstructor
@Getter
public enum DependencyTypeEnum implements N2oEnum {
    FETCH("fetch"),
    VALIDATE("validate"),
    COPY("copy");

    private final String id;
}
