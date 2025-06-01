package net.n2oapp.framework.api.metadata.compile.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum ColorEnum implements N2oEnum {
    PRIMARY("primary"),
    SECONDARY("secondary"),
    SUCCESS("success"),
    DANGER("danger"),
    WARNING("warning"),
    INFO("info"),
    LIGHT("light"),
    DARK("dark");

    private final String id;
}