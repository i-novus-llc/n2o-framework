package net.n2oapp.framework.autotest.api.component.widget.chart;

/**
 * Виджет - диаграмма-область для автотестирования
 */
public interface AreaChartWidget extends ChartWidget {

    /**
     * Возвращает область диаграммы по номеру
     *
     * @param index номер области
     * @return область диаграммы для автотестирования
     */
    Area area(int index);
}