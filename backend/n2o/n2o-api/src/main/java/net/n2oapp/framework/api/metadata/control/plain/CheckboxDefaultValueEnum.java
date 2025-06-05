package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum CheckboxDefaultValueEnum implements N2oEnum {
    NULL("null"),
    FALSE("false");

    private final String id;
}