package net.n2oapp.framework.autotest.api.component.cell;

/**
 * Ячейка таблицы с ссылкой для автотестирования
 */
public interface LinkCell extends Cell {

    void click();

    void hrefShouldHave(String href);

    void textShouldHave(String text);

    void shouldNotHaveText();
}
