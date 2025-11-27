package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Горизонтальное выравнивание элементов
 */
@RequiredArgsConstructor
@Getter
public enum JustifyEnum implements N2oEnum {
    START("start"),
    END("end"),
    CENTER("center"),
    SPACE_AROUND("space-around"),
    SPACE_BETWEEN("space-between"),
    SPACE_EVENLY("space-evenly");

    private final String id;
}
