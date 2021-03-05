package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oBarChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oBarChartItem;
import net.n2oapp.framework.api.metadata.meta.widget.chart.BarChart;
import net.n2oapp.framework.api.metadata.meta.widget.chart.BarChartItem;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartType;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция компонента гистограммы
 */
@Component
public class BarChartCompiler extends StandardChartCompiler<BarChart, N2oBarChart> {

    @Override
    public BarChart compile(N2oBarChart source, CompileContext<?, ?> context, CompileProcessor p) {
        BarChart chart = new BarChart();
        build(chart, source, context, p, property("n2o.api.widget.chart.bar.src"));
        chart.setType(ChartType.bar);
        for (N2oBarChartItem item : source.getItems()) {
            BarChartItem component = new BarChartItem();
            component.setFieldId(item.getFieldId());
            component.setLabel(item.getLabel());
            component.setColor(item.getColor());
            component.setHasLabel(p.cast(item.getHasLabel(), p.resolve(property("n2o.api.widget.chart.has_label"), Boolean.class)));
            chart.addItem(component);
        }
        return compileStandardChart(chart, source, context, p);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oBarChart.class;
    }
}
