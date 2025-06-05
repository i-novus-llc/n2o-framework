package net.n2oapp.framework.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * User: operhod
 * Date: 18.11.13
 * Time: 14:49
 */
@RequiredArgsConstructor
@Getter
public enum SeverityTypeEnum implements N2oEnum {
    DANGER("danger"),
    WARNING("warning"),
    INFO("info"),
    SUCCESS("success");

    private final String id;
}