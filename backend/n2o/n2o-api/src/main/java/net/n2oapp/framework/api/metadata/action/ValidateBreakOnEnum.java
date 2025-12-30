package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum ValidateBreakOnEnum implements N2oEnum {
    DANGER("danger"),
    WARNING("warning"),
    FALSE("false");

    private final String id;
}
