package net.n2oapp.framework.autotest.api.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.widget.Widget;

/**
 * Виджет «Диаграмма» для автотестирования
 */
public interface ChartWidget extends Widget {

    /**
     * Проверка ширины виджета
     *
     * @param width ожидаемая ширина
     */
    void shouldHaveWidth(int width);

    /**
     * Проверка высоты виджета
     *
     * @param height ожидаемая высота
     */
    void shouldHaveHeight(int height);

    /**
     * Проверка наличия оси X
     */
    void shouldHaveXAxis();

    /**
     * Проверка наличия оси Y
     */
    void shouldHaveYAxis();

    /**
     * Проверка наличия легенды
     */
    void shouldHaveLegend();

    /**
     * Проверка наличия сетки
     */
    void shouldHaveGrid();

    /**
     * Проверка ширины области построения графика
     *
     * @param width ожидаемая ширина
     */
    void shouldHaveChartWidth(int width);

    /**
     * Проверка высоты области построения графика
     *
     * @param height ожидаемая высота
     */
    void shouldHaveChartHeight(int height);

    /**
     * Проверка текста элемента легенды
     *
     * @param index индекс элемента легенды
     * @param text  ожидаемый текст
     */
    void shouldHaveLegendItem(int index, String text);

    /**
     * Проверка текста подписи на оси X
     *
     * @param index индекс подписи
     * @param text  ожидаемый текст
     */
    void shouldHaveXAxisTick(int index, String text);

    /**
     * Проверка количества подписей на оси X
     *
     * @param count ожидаемое количество
     */
    void shouldHaveXAxisTicksCount(int count);

    /**
     * Проверка текста подписи на оси Y
     *
     * @param index индекс подписи
     * @param text  ожидаемый текст
     */
    void shouldHaveYAxisTick(int index, String text);

    /**
     * Проверка количества подписей на оси Y
     *
     * @param count ожидаемое количество
     */
    void shouldHaveYAxisTicksCount(int count);
}