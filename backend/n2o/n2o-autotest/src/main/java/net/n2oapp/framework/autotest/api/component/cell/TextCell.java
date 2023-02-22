package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Ячейка с текстом для автотестирования
 */
public interface TextCell extends Cell {
    /**
     * Проверка точного соответствия (без учета регистра) текста
     * @param text ожидаемый текст
     */
    void shouldHaveText(String text);

    /**
     * Проверка соответствия подтекста
     * @param text ожидаемый подтекст
     */
    void shouldHaveSubText(String... text);

    /**
     * Проверка существования иконки и соответствия ее позиции
     * @param position ожидаемая позиция иконки
     */
    void shouldHaveIconPosition(Position position);
}
