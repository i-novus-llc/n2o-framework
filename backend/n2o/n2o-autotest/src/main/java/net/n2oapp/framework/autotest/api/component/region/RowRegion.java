package net.n2oapp.framework.autotest.api.component.region;

/**
 * Интерфейс региона {@code <row>} для автотестирования
 */
public interface RowRegion extends Region {
    /**
     * @return Элемент региона (виджет/регион) для автотестирования
     */
    RegionItems content();

    /**
     * Проверка выравнивания ячейки
     *
     * @param alignment Ожидаемое выравнивание
     */
    void shouldHaveAlignment(String alignment);

    /**
     * Проверка количества колонок
     *
     * @param columns Ожидаемое количество
     */
    void shouldHaveColumns(String columns);

    /**
     * Проверка того, что есть перенос контента
     */
    void shouldBeWrapped();

    /**
     * Проверка того, что нет переноса контента
     */
    void shouldNotBeWrapped();

    /**
     * Проверка горизонтального выравнивания
     *
     * @param justify ожидаемое количество
     */

    void shouldHaveJustify(String justify);
}