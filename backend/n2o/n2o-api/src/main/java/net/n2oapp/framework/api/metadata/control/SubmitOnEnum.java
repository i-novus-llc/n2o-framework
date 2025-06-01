package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Действие, по которому происходит отправка данных
 */
@RequiredArgsConstructor
@Getter
public enum SubmitOnEnum implements N2oEnum {
    CHANGE("change"),
    BLUR("blur");

    private final String id;
}
