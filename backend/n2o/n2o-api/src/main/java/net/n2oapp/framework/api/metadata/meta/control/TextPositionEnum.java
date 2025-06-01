package net.n2oapp.framework.api.metadata.meta.control;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Виды позиций текста относительно элемента
 */
@RequiredArgsConstructor
@Getter
public enum TextPositionEnum implements N2oEnum {
    TOP("top"),
    LEFT("left"),
    RIGHT("right"),
    BOTTOM("bottom");

    private final String id;
}
