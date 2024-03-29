package net.n2oapp.framework.autotest.api.component.widget.calendar.view;

import net.n2oapp.framework.autotest.api.component.Component;

import java.time.Duration;

/**
 * Вид отображения календаря 'Повестка дня' для автотестирования
 */
public interface CalendarAgendaView extends Component {

    /**
     * Проверка количества строк в календаре
     * @param size ожидаемое количество строк
     */
    void shouldHaveSize(int size);

    /**
     * Проверка наличия события в ячейке даты по индексу
     * @param index проверяемая ячейка
     * @param date ожидаемый текст события
     */
    void eventShouldHaveDate(int index, String date, Duration... duration);

    /**
     * Проверка наличия события в ячейке времени по индексу
     * @param index проверяемая ячейка
     * @param time ожидаемый текст события
     */
    void eventShouldHaveTime(int index, String time, Duration... duration);

    /**
     * Проверка наименования события в ячейке по индексу
     * @param index проверяемая ячейка
     * @param name ожидаемый текст события
     */
    void eventShouldHaveName(int index, String name, Duration... duration);
}
