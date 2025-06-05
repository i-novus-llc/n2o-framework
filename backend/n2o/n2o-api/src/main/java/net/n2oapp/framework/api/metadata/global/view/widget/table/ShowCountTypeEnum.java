package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum ShowCountTypeEnum implements N2oEnum {
    ALWAYS("always"),
    NEVER("never"),
    BY_REQUEST("by-request");

    private final String id;
}