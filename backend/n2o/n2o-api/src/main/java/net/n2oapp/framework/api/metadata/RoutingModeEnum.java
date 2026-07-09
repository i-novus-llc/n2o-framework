package net.n2oapp.framework.api.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum RoutingModeEnum implements N2oEnum {
    OLD("old"),
    NEW("new");

    private final String id;
}
