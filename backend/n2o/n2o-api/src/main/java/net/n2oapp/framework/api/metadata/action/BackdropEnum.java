package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Наличие фона модального окна
 */

@RequiredArgsConstructor
@Getter
public enum BackdropEnum implements N2oEnum {
    TRUE("true"),
    FALSE("false"),
    STATIC("static");

    private final String id;

    /**
     * Значение для клиентской модели.
     * На клиенте backdrop сравнивается строго (=== true, === 'static'),
     * поэтому true и false передаются как boolean, а не как строка
     *
     * @return {@link Boolean} для TRUE и FALSE, {@link String} "static" для STATIC
     */
    public Object getClientValue() {
        return this == STATIC ? id : Boolean.valueOf(id);
    }
}