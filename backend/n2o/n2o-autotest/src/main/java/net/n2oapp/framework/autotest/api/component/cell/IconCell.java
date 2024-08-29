package net.n2oapp.framework.autotest.api.component.cell;

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

}
