package net.n2oapp.framework.autotest.api.component.widget.chart;

/**
 * Виджет - диаграмма-область для автотестирования
 */
public interface AreaChartWidget extends ChartWidget {

    /**
     * Возвращает диаграму для автотестирования по номеру
     * @param index номер диаграммы
     * @return Область виджета диаграмма-область для автотестирования
     */
    Area area(int index);
}
