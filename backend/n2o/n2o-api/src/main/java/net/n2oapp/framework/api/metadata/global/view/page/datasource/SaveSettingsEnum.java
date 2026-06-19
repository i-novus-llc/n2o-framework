package net.n2oapp.framework.api.metadata.global.view.page.datasource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum SaveSettingsEnum implements N2oEnum {
    PAGE("page"),
    SIZE("size"),
    SORTING("sorting");

    private final String id;
}