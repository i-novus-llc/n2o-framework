package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum SearchSideEnum implements N2oEnum {
    CLIENT("client"),
    SERVER("server");

    private final String id;
}