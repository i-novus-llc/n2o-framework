package net.n2oapp.framework.autotest.api.component.region;

/**
 * Интерфейс региона {@code <col>} для автотестирования
 */
public interface ColRegion extends Region {
    /**
     * @return Элемент региона (виджет/регион) для автотестирования
     */
    RegionItems content();

    /**
     * Проверка занимаемого количества колонок
     *
     * @param size Ожидаемое количество
     */
    void shouldHaveSize(int size);

    /**
     * Проверка отступа слева (в колонках)
     *
     * @param offset Ожидаемый отступ
     */
    void shouldHaveOffset(int offset);
}
