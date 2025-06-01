package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Тип слияния данных
 */
@RequiredArgsConstructor
@Getter
public enum CopyModeEnum implements N2oEnum {
    MERGE("merge"),
    REPLACE("replace"),
    ADD("add");

    private final String id;
}
