package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Режим слияния данных
 */

@RequiredArgsConstructor
@Getter
public enum MergeModeEnum implements N2oEnum {
    MERGE("merge"),
    REPLACE("replace"),
    ADD("add");

    private final String id;
}
