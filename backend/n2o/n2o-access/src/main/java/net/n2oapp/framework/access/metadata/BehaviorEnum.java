package net.n2oapp.framework.access.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum BehaviorEnum implements N2oEnum {
    HIDE("hide"),
    DISABLE("disable");

    private final String id;
}
