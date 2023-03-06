package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.api.component.PopupControl;

/**
 * Компонент ввода времени для автотестирования
 */
public interface TimePicker extends Control, PopupControl {

    /**
     * Выбор полного времени
     *
     * @param hours часы в формате 24
     * @param minutes минуты
     * @param seconds секунды
     */
    void selectHoursMinutesSeconds(String hours, String minutes, String seconds);

    /**
     * Выбор минут и секунд
     *
     * @param minutes минуты
     * @param seconds секунды
     */
    void selectMinutesSeconds(String minutes, String seconds);

    /**
     * Выбор часов
     * @param hours выбираемые часы
     */
    void selectHours(String hours);

    /**
     * Выбор минут
     * @param minutes выбираемые минуты
     */
    void selectMinutes(String minutes);

    /**
     * Проверка выбранного времени
     * @param hours ожидаемые часы
     * @param minutes ожидаемые минуты
     * @param seconds ожидаемые секунды
     */
    void shouldSelectedHoursMinutesSeconds(String hours, String minutes, String seconds);

    /**
     * Проверка выбранного времени без секунд
     * @param hours ожидаемые часы
     * @param minutes ожидаемые минуты
     */
    void shouldSelectedHoursMinutes(String hours, String minutes);

    /**
     * Проверка выбранных часов
     * @param hours ожидаемые часы
     */
    void shouldSelectedHours(String hours);

    /**
     * Проверка выбранных минут
     * @param minutes ожидаемые минут
     */
    void shouldSelectedMinutes(String minutes);

    /**
     * Проверка соответствия префикса
     * @param prefix ожидаемый префикс
     */
    void shouldHavePrefix(String prefix);

    /**
     * Проверка отсутствия префикса
     */
    void shouldNotHavePrefix();
}
