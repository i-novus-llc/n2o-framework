package net.n2oapp.framework.api.metadata.meta.widget;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Положение сообщения при фиксации
 */
@RequiredArgsConstructor
@Getter
public enum MessagePlacementEnum implements N2oEnum {
    TOP("top"),
    BOTTOM("bottom"),
    TOP_LEFT("topLeft"),
    TOP_RIGHT("topRight"),
    BOTTOM_LEFT("bottomLeft"),
    BOTTOM_RIGHT("bottomRight");

    private final String id;
}