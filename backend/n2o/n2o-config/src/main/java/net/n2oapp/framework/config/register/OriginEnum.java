package net.n2oapp.framework.config.register;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Происхождение метаданной
 */

@RequiredArgsConstructor
@Getter
public enum OriginEnum implements N2oEnum {
    XML("xml"),
    @Deprecated
    COMPILE("compile"),
    DYNAMIC("dynamic");

    private final String id;
}
