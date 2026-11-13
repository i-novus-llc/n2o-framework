package net.n2oapp.framework.autotest.api.component.widget.chart;

/**
 * Виджет - линейный график для автотестирования
 */
public interface LineChartWidget extends ChartWidget {

    /**
     * Возвращает линию графика по номеру
     *
     * @param index номер линии
     * @return линия для автотестирования
     */
    Line line(int index);
}