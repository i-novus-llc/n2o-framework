package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.autotest.api.component.Tooltip;

import java.time.Duration;

/**
 * Ячейка таблицы с иконкой для автотестирования
 */
public interface IconCell extends Cell {
    /**
     * Проверка текста в ячейки на точное соответствие (без учета регистра) ожидаемому значению
     * @param text ожидаемый текст
     */
    void shouldHaveText(String text, Duration... duration);

    /**
     * Наведение мыши на ячейку
     */
    void hover();

    /**
     * Возвращает тултим ячейки
     * @return Тултип для автотестирования
     */
    Tooltip tooltip();
}
