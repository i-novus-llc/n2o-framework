package net.n2oapp.framework.autotest.api.component.widget.chart;

/**
 * Виджет - круговая диаграмма для автотестирования
 */
public interface PieChartWidget extends ChartWidget {

    /**
     * Возвращает круговую диаграмму
     *
     * @return круговая диаграмма для автотестирования
     */
    Pie pie();
}