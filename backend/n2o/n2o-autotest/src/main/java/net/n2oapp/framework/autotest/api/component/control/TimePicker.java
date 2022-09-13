package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.api.component.Expandable;
import net.n2oapp.framework.autotest.api.component.PopupControl;

/**
 * Компонент ввода времени для автотестирования
 */
public interface TimePicker extends Control, PopupControl {

    void selectHoursMinutesSeconds(String hours, String minutes, String seconds);

    void selectMinutesSeconds(String minutes, String seconds);

    void selectHours(String hours);

    void selectMinutes(String minutes);

    void shouldSelectedHoursMinutesSeconds(String hours, String minutes, String seconds);

    void shouldSelectedHoursMinutes(String hours, String minutes);

    void shouldSelectedHours(String hours);

    void shouldSelectedMinutes(String minutes);

    void shouldHavePrefix(String prefix);

    void shouldNotHavePrefix();
}
