package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum MaskPasteModeEnum implements N2oEnum {
    STRICT("strict"),
    FREE("free");

    private final String id;
}