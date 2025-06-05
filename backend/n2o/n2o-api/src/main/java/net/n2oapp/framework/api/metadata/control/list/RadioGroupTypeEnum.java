package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum RadioGroupTypeEnum implements N2oEnum {
    DEFAULT("default"),
    BTN("btn"),
    TABS("tabs");

    private final String id;
}