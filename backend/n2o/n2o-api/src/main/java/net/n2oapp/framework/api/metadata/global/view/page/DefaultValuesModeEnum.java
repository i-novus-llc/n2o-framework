package net.n2oapp.framework.api.metadata.global.view.page;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Режим подстановки значений по умолчанию
 */
@RequiredArgsConstructor
@Getter
public enum DefaultValuesModeEnum implements N2oEnum {
    QUERY("query"),
    DEFAULTS("defaults"),
    MERGE("merge");

    private final String id;
}
