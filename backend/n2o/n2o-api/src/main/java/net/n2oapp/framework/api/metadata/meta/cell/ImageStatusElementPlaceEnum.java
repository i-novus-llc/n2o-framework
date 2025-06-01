package net.n2oapp.framework.api.metadata.meta.cell;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum ImageStatusElementPlaceEnum implements N2oEnum {
    TOP_LEFT("topLeft"),
    TOP_RIGHT("topRight"),
    BOTTOM_LEFT("bottomLeft"),
    BOTTOM_RIGHT("bottomRight");

    private final String id;
}
