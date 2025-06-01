package net.n2oapp.framework.api.metadata.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * User: operhod
 * Date: 14.01.14
 * Time: 12:41
 */
@RequiredArgsConstructor
@Getter
public enum LevelEnum implements N2oEnum {
    FIRST("first"),
    SECOND("second"),
    LAST("last");

    private final String id;
}