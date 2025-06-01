package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Режим форматирования дробной части компонента <input-money/>
 */
@RequiredArgsConstructor
@Getter
public enum FractionFormattingEnum implements N2oEnum {
    /**
     * Форматирования дробной части отключено
     */
    OFF("off"),

    /**
     * Ручное форматирование дробной части
     */
    MANUAL("manual"),

    /**
     * Автозаполнение дробной части
     */
    AUTO("auto");

    private final String id;
}
