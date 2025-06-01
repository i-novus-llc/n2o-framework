package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum CheckingStrategyEnum implements N2oEnum {
    ALL("all"),
    PARENT("parent"),
    CHILD("child");

    private final String id;
}
