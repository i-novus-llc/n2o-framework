package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum FilterPositionEnum implements N2oEnum {
    TOP("top"),
    LEFT("left");

    private final String id;
}