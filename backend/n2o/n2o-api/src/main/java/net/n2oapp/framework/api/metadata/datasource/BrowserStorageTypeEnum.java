package net.n2oapp.framework.api.metadata.datasource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Тип хранилища браузера
 */
@RequiredArgsConstructor
@Getter
public enum BrowserStorageTypeEnum implements N2oEnum {
    SESSION_STORAGE("sessionStorage"),
    LOCAL_STORAGE("localStorage");

    private final String id;
}