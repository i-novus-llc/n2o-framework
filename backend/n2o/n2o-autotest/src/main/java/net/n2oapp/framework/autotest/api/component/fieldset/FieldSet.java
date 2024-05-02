package net.n2oapp.framework.autotest.api.component.fieldset;

import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.badge.Badge;

import java.time.Duration;

/**
 * Филдсет для автотестирования
 */
public interface FieldSet extends Component, Help, Badge {
    /**
     * Проверка того, что филдсет пустой
     */
    void shouldBeEmpty();

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
     * Проверка описание на соответствие
     * @param description ожидаемое описание
     */
    void shouldHaveDescription(String description, Duration... duration);

    /**
     * Проверка того, что метки не существует
     */
    void shouldNotHaveDescription();
}
