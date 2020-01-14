package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oStandardChartComponent;
import net.n2oapp.framework.api.metadata.meta.widget.chart.*;
import org.springframework.stereotype.Component;

/**
 * Компиляция стандартного графика
 */
@Component
public abstract class StandardChartCompiler<D extends StandardChartWidgetComponent, S extends N2oStandardChartComponent> extends AbstractChartCompiler<D, S> {

    public D compileStandardChart(D chart, S source, CompileContext<?, ?> context, CompileProcessor p) {
        ChartAxis xAxis = new ChartAxis();
        xAxis.setDataKey(source.getXAxisDataKey());
        xAxis.setTickCount(source.getXAxisTickCount());
        chart.setXAxis(xAxis);
        ChartAxis yAxis = new ChartAxis();
        yAxis.setDataKey(source.getYAxisDataKey());
        yAxis.setTickCount(source.getYAxisTickCount());
        chart.setYAxis(yAxis);
        ChartGrid grid = new ChartGrid();
        grid.setX(source.getGridX());
        grid.setY(source.getGridY());
        grid.setWidth(source.getGridWidth());
        grid.setHeight(source.getGridHeight());
        chart.setGrid(grid);
        ChartTooltip tooltip = new ChartTooltip();
        tooltip.setSeparator(source.getTooltipSeparator());
        chart.setTooltip(tooltip);
        ChartLegend legend = new ChartLegend();
        legend.setWidth(source.getLegendWidth());
        legend.setHeight(source.getLegendHeight());
        chart.setLegend(legend);
        return chart;
    }
}
