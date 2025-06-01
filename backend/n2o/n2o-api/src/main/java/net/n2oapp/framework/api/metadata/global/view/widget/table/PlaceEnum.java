package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Место расположения пагинации
 */
@RequiredArgsConstructor
@Getter
public enum PlaceEnum implements N2oEnum {
    TOP_LEFT("topLeft"),
    TOP_RIGHT("topRight"),
    BOTTOM_LEFT("bottomLeft"),
    BOTTOM_RIGHT("bottomRight"),
    TOP_CENTER("topCenter"),
    BOTTOM_CENTER("bottomCenter");

    private final String id;
}