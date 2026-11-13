package net.n2oapp.framework.autotest.api.component.widget.chart;

/**
 * Виджет - гистограмма для автотестирования
 */
public interface BarChartWidget extends ChartWidget {

    /**
     * Возвращает столбец гистограммы по номеру
     *
     * @param index номер столбца
     * @return столбец для автотестирования
     */
    Bar bar(int index);
}