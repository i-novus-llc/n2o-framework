package net.n2oapp.framework.autotest.api.component.fieldset;

import net.n2oapp.framework.autotest.api.component.Component;

import java.time.Duration;

/**
 * Филдсет для автотестирования
 */
public interface FieldSet extends Component, Help {
    /**
     * Проверка того, что филдсет пустой
     */
    void shouldBeEmpty();

    /**
     * Устарело, вместо этого следует использовать shouldBeHidden()
     */
    @Deprecated
    void shouldNotBeVisible();

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
