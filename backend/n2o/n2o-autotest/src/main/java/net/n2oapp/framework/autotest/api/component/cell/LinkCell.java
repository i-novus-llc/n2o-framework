package net.n2oapp.framework.autotest.api.component.cell;

/**
 * Ячейка таблицы с ссылкой для автотестирования
 */
public interface LinkCell extends Cell {

    /**
     * Клик по ячейке
     */
    void click();

    /**
     * Проверка того, что ячейка содержит ожидаемую ссылку
     * @param href ожидаемая ссылка
     */
    void shouldHaveHref(String href);

    /**
     * Проверка того, что ячейка содержит точное значение ожидаемого текст (без учета регистра)
     * @param text ожидаемый текст
     */
    void shouldHaveText(String text);

    /**
     * Проверка того, что ячейка не содержит текста
     */
    void shouldNotHaveText();
}
