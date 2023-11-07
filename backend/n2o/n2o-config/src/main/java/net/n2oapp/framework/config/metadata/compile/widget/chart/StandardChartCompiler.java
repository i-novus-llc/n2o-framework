package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oStandardChart;
import net.n2oapp.framework.api.metadata.meta.widget.chart.*;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция стандартного компонента диаграммы
 */
@Component
public abstract class StandardChartCompiler<D extends StandardChartWidgetComponent, S extends N2oStandardChart> extends AbstractChartCompiler<D, S> {

    protected D compileStandardChart(D chart, S source, CompileProcessor p) {
        ChartAxis xAxis = new ChartAxis();
        xAxis.setFieldId(source.getXFieldId());
        xAxis.setPosition((castDefault(source.getXPosition(),
                () -> p.resolve(property("n2o.api.widget.chart.axis.x_position")),
                () -> N2oStandardChart.XAxisPosition.class))
                .toString());
        xAxis.setHasLabel(castDefault(source.getXHasLabel(),
                () -> p.resolve(property("n2o.api.widget.chart.axis.has_label"), Boolean.class)));
        chart.setXAxis(xAxis);

        ChartAxis yAxis = new ChartAxis();
        yAxis.setFieldId(source.getYFieldId());
        yAxis.setPosition((castDefault(source.getYPosition(),
                () -> p.resolve(property("n2o.api.widget.chart.axis.y_position")),
                () -> N2oStandardChart.YAxisPosition.class))
                .toString());
        yAxis.setHasLabel(castDefault(source.getYHasLabel(),
                () -> p.resolve(property("n2o.api.widget.chart.axis.has_label"), Boolean.class)));
        yAxis.setMin(source.getYMin());
        yAxis.setMax(source.getYMax());
        chart.setYAxis(yAxis);

        ChartGrid grid = new ChartGrid();
        grid.setStrokeDashArray(source.getGridStrokeDasharray());
        grid.setHorizontal(castDefault(source.getGridHorizontal(),
                () -> p.resolve(property("n2o.api.widget.chart.grid.horizontal"), Boolean.class)));
        grid.setVertical(castDefault(source.getGridVertical(),
                () -> p.resolve(property("n2o.api.widget.chart.grid.vertical"), Boolean.class)));
        chart.setGrid(grid);

        ChartTooltip tooltip = new ChartTooltip();
        tooltip.setSeparator(castDefault(source.getTooltipSeparator(),
                () -> p.resolve(property("n2o.api.widget.chart.tooltip.separator"), String.class), () -> " "));
        chart.setTooltip(tooltip);

        ChartLegend legend = new ChartLegend();
        legend.setIconType(castDefault(source.getLegendIconType(),
                () -> p.resolve(property("n2o.api.widget.chart.legend.icon_type"), ChartLegendIconType.class)));
        chart.setLegend(legend);

        return chart;
    }
}
