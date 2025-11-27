package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Вертикальное выравнивание элементов
 */
@RequiredArgsConstructor
@Getter
public enum AlignEnum implements N2oEnum {
    TOP("top"),
    MIDDLE("middle"),
    BOTTOM("bottom"),
    STRETCH("stretch");

    private final String id;
}