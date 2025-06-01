package net.n2oapp.framework.api.rest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum ControllerTypeEnum implements N2oEnum {
    QUERY("query"),
    MERGE("merge"),
    OPERATION("operation"),
    VALIDATION("validation");

    private final String id;
}
