package net.n2oapp.framework.api.metadata.meta.badge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Позиция left/right
 */
@RequiredArgsConstructor
@Getter
public enum PositionEnum implements N2oEnum {
    LEFT("left"),
    RIGHT("right");

    private final String id;
}