package net.n2oapp.framework.autotest.api.component.region;

/**
 * Интерфейс региона {@code <flex-row>} для автотестирования
 */
public interface FlexRowRegion extends Region {
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
     * @param justify Ожидаемое количество
     */

    void shouldHaveJustify(String justify);
}