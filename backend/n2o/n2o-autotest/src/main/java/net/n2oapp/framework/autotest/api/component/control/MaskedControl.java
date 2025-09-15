package net.n2oapp.framework.autotest.api.component.control;

import com.codeborne.selenide.WebElementCondition;

import java.time.Duration;

/**
 * Интерфейс поля ввода с маской для автотестов
 */
public interface MaskedControl extends Control {
    /**
     * Установка значения в поле ввода
     *
     * @param value Устанавливаемое значение
     */
    void setValue(String value);

    /**
     * Проверка соответствия текста подсказки для ввода
     *
     * @param value Ожидаемый текст
     */
    void shouldHavePlaceholder(String value);

    /**
     * Проверка сообщения валидации на соответствие условию
     *
     * @param condition Ожидаемое условие
     */
    void shouldHaveInvalidText(WebElementCondition condition, Duration... duration);
}