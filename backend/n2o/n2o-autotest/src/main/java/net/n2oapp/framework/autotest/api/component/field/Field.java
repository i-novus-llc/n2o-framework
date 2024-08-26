package net.n2oapp.framework.autotest.api.component.field;

import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.fieldset.Help;

import java.time.Duration;

/**
 * Поле формы для автотестирования
 */
public interface Field extends Component, Help {
    /**
     * Проверка метки на соответствие
     * @param label ожидаемое значение метки
     */
    void shouldHaveLabel(String label, Duration... duration);

    /**
     * Проверка того, что метки не существует
     */
    void shouldNotHaveLabel();

    /**
     * Проверка пустоты поля
     */
    void shouldHaveEmptyLabel();

}
