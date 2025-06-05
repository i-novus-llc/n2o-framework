package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum PageRefEnum implements N2oEnum {
    THIS("this"),
    PARENT("parent");

    private final String id;
}