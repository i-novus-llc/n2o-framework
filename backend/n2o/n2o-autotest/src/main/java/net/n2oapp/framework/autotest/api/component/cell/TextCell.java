package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Ячейка с текстом для автотестирования
 */
public interface TextCell extends Cell {
    void shouldHaveText(String text);

    void shouldHaveSubText(String... text);

    void shouldHaveIconPosition(Position position);
}
