package net.n2oapp.framework.autotest.api.component.cell;

/**
 * Ячейка с текстом для автотестирования
 */
public interface TextCell extends Cell {
    void textShouldHave(String text);

    void subTextShouldHave(String... text);

    void shouldBeIconPosition(String position);
}
