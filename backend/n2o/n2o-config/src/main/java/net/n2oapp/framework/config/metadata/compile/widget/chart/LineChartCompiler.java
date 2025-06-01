package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oLineChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oLineChartItem;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartLineTypeEnum;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartTypeEnum;
import net.n2oapp.framework.api.metadata.meta.widget.chart.LineChart;
import net.n2oapp.framework.api.metadata.meta.widget.chart.LineChartItem;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция компонента линейного графика
 */
@Component
public class LineChartCompiler extends StandardChartCompiler<LineChart, N2oLineChart> {

    @Override
    public LineChart compile(N2oLineChart source, CompileContext<?, ?> context, CompileProcessor p) {
        LineChart chart = new LineChart();
        build(chart, source, p, property("n2o.api.widget.chart.line.src"));
        chart.setType(ChartTypeEnum.LINE);
        for (N2oLineChartItem item : source.getItems()) {
            LineChartItem component = new LineChartItem();
            component.setFieldId(item.getFieldId());
            component.setLabel(item.getLabel());
            component.setType(castDefault(item.getType(),
                    () -> p.resolve(property("n2o.api.widget.chart.line.type"), ChartLineTypeEnum.class)));
            component.setColor(item.getColor());
            component.setHasLabel(castDefault(item.getHasLabel(),
                    () -> p.resolve(property("n2o.api.widget.chart.line.has_label"), Boolean.class)));
            chart.addItem(component);
        }
        return compileStandardChart(chart, source, p);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oLineChart.class;
    }
}
