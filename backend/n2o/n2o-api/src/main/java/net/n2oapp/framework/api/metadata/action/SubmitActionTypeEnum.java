package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Тип действия кнопки отправки формы
 */
@RequiredArgsConstructor
@Getter
public enum SubmitActionTypeEnum implements N2oEnum {
    INVOKE("invoke"),
    COPY("copy");

    private final String id;
}
