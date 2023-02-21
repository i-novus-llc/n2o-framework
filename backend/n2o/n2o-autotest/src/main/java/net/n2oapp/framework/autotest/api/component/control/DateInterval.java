package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.api.component.PopupControl;

/**
 * Компонент ввода интервала дат для автотестирования
 */
public interface DateInterval extends Control, PopupControl {

    /**
     * Проверка того, что дата начала не задана
     */
    void beginShouldBeEmpty();

    /**
     * Проверка того, что дата конца не задана
     */
    void endShouldBeEmpty();

    /**
     * Установка значения в поле даты начала
     * @param value значение даты в формате день.месяц.год
     */
    void setValueInBegin(String value);

    /**
     * Установка значения в поле даты конца
     * @param value значение даты в формате день.месяц.год
     */
    void setValueInEnd(String value);

    /**
     * Провека того, что в поле даты начала установлено соответствующее значение
     * @param value ожидаемая дата в формате день.месяц.год
     */
    void beginShouldHaveValue(String value);

    /**
     * Провека того, что в поле даты конца установлено соответствующее значение
     * @param value ожидаемая дата в формате день.месяц.год
     */
    void endShouldHaveValue(String value);

    /**
     * Клик по иконке календаря в поле ввода для открытия календаря
     */
    void clickCalendarButton();

    /**
     * Проверка выбранности дня в календаре у поля ввода начала
     * @param day проверяемый день
     */
    void beginDayShouldBeActive(String day);

    /**
     * Проверка выбранности дня в календаре у поля ввода конца
     * @param day проверяемый день
     */
    void endDayShouldBeActive(String day);

    /**
     * Проверка недоступности дня в календаре у поля ввода начала
     * @param day проверяемый день
     */
    void beginDayShouldBeDisabled(String day);

    /**
     * Проверка недоступности дня в календаре у поля ввода конца
     * @param day проверяемый день
     */
    void endDayShouldBeDisabled(String day);

    /**
     * Проверка доступности дня в календаре у поля ввода начала
     * @param day проверяемый день
     */
    void beginDayShouldBeEnabled(String day);

    /**
     * Проверка доступности дня в календаре у поля ввода конца
     * @param day проверяемый день
     */
    void endDayShouldBeEnabled(String day);

    /**
     * Клик по дню в календаре начала для выбора
     * @param day выбираемый день
     */
    void clickBeginDay(String day);

    /**
     * Клик по дню в календаре начала для выбора
     * @param day выбираемый день
     */
    void clickEndDay(String day);

    /**
     * Проверка текущего месяца в календаре начала
     * @param month ожидаемый текущий месяц
     */
    void beginCurrentMonthShouldHaveValue(String month);

    /**
     * Проверка текущего месяца в календаре конца
     * @param month ожидаемый текущий месяц
     */
    void endCurrentMonthShouldHaveValue(String month);

    /**
     * Проверка текущего года в календаре начала
     * @param year ожидаемый текущий год
     */
    void beginCurrentYearShouldHaveValue(String year);

    /**
     * Проверка текущего года в календаре конца
     * @param year ожидаемый текущий год
     */
    void endCurrentYearShouldHaveValue(String year);

    /**
     * Клик по кнопке для перехода к предыдущему месяцу в календаре начала
     */
    void clickBeginMonthPreviousButton();

    /**
     * Клик по кнопке для перехода к предыдущему месяцу в календаре конца
     */
    void clickEndMonthPreviousButton();

    /**
     * Клик по кнопке для перехода к следующему месяцу в календаре начала
     */
    void clickBeginMonthNextButton();

    /**
     * Клик по кнопке для перехода к следующему месяцу в календаре конца
     */
    void clickEndMonthNextButton();

    /**
     * Установка времени в поле ввода даты начала
     *
     * @param hours часы в формате 24
     * @param minutes минуты
     * @param seconds секунды
     */
    void beginTimeSetValue(String hours, String minutes, String seconds);

    /**
     * Установка времени в поле ввода даты конца
     *
     * @param hours часы в формате 24
     * @param minutes минуты
     * @param seconds секунды
     */
    void endTimeSetValue(String hours, String minutes, String seconds);
}
