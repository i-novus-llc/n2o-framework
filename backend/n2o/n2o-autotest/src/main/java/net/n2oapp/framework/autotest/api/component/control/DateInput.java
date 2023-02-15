package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.api.component.PopupControl;

/**
 * Компонент ввода даты для автотестирования
 */
public interface DateInput extends Control, PopupControl {

    /**
     * Возвращает значение из поля ввода
     * @return Выбранная дата
     */
    String getValue();

    /**
     * Установка значения в поле ввода даты
     *
     * <p>
     *     Пример использования: {@code
     *          date.setValue("15.02.2020");
     *     }
     * </p>
     * @param value значение даты
     */
    void setValue(String value);

    /**
     * Проверка подсказки для ввода
     * @param value ожидаемое значение подсказки
     */
    void shouldHavePlaceholder(String value);

    /**
     * Ввод времени
     *
     * <p>
     *     Пример использования: {@code
     *          date.timeVal("23", "59", "58");
     *     }
     * </p>
     *
     * @param hours часы в формате 24
     * @param minutes минуты
     * @param seconds секунды
     */
    void timeVal(String hours, String minutes, String seconds);

    /**
     * Клик по иконке календаря в поле ввода для раскрытия календаря
     */
    void clickCalendarButton();

    /**
     * Проверка выбранности дня в календаре
     * @param day проверяемый день
     */
    void shouldBeActiveDay(String day);

    /**
     * Клик по дню в календаре для выбора
     * @param day выбираемый день
     */
    void clickDay(String day);

    /**
     * Проверка недоступности дня в календаре для выбора
     * @param day проверяемый день
     */
    void shouldBeDisableDay(String day);

    /**
     * Проверка доступности дня в календаре для выбора
     * @param day проверяемый день
     */
    void shouldNotBeDisableDay(String day);

    /**
     * Проверка текущего месяца в календаре
     * @param month ожидаемый текущий месяц
     */
    void shouldHaveCurrentMonth(String month);

    /**
     * Проверка текущего года в календаре
     * @param year ожидаемый текущий год
     */
    void shouldHaveCurrentYear(String year);

    /**
     * Клик по кнопке для перехода к предыдущему месяцу в календаре
     */
    void clickPreviousMonthButton();

    /**
     * Клик по кнопке для перехода к следующему месяцу в календаре
     */
    void clickNextMonthButton();
}
