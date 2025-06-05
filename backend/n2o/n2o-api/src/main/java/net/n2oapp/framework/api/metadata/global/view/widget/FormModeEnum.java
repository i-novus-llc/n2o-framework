package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Режим формы
 */
@RequiredArgsConstructor
@Getter
public enum FormModeEnum implements N2oEnum {
    ONE_MODEL("one-model"),
    TWO_MODELS("two-models");

    private final String id;
}