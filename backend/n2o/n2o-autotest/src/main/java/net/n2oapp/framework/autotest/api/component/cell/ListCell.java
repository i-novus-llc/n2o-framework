package net.n2oapp.framework.autotest.api.component.cell;

import java.time.Duration;

/**
 * Ячейка таблицы со списком для автотестирования
 */
public interface ListCell extends Cell {
    /**
     * Проверяет количество баджей(Badge) в ячейке
     *
     * @param count ожидаемое количество баджей
     */
    void shouldHaveSize(int count);

    /**
     * Проверяет количество ячеек link
     *
     * @param count ожидаемое количество ячеек link
     */
    void shouldHaveInnerLinksSize(int count);

    /**
     * Проверяет количество ячеек badge
     *
     * @param count ожидаемое количество ячеек badge
     */
    void shouldHaveInnerBadgesSize(int count);

    /**
     * Проверяет текст в бадже(Badge)
     *
     * @param index номер проверяемого баджа в ячейке
     * @param val   ожидаемый текст баджа
     */
    void shouldHaveText(int index, String val, Duration... duration);

    /**
     * Проверяет наличие ячейки text
     *
     * @param index номер проверяемой ячейки text
     * @param val   ожидаемый текст ячейки text
     */
    void shouldHaveInnerText(int index, String val, Duration... duration);

    /**
     * Проверяет наличие ячейки link
     *
     * @param index номер проверяемой ячейки link
     * @param val   ожидаемый текст ячейки link
     */
    void shouldHaveInnerLink(int index, String val, Duration... duration);

    /**
     * Проверяет наличие ячейки badge
     *
     * @param index номер проверяемой ячейки badge
     * @param val   ожидаемый текст ячейки badge
     */
    void shouldHaveInnerBadge(int index, String val, Duration... duration);

    /**
     * Проверяет разделитель
     *
     * @param val ожидаемый разделитель
     */
    void shouldHaveSeparator(String val, Duration... duration);

    /**
     * Проверяет, что элементы находятся в одной строке
     */
    void shouldBeInline();

    /**
     * Проверяет, что элементы не находятся в одной строке
     */
    void shouldNotBeInline();

    /**
     * Проверка того, что ячейка содержит ожидаемую ссылку
     *
     * @param href ожидаемая ссылка
     */
    void shouldHaveHref(int index, String href);

}


