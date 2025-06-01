package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oAreaChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oAreaChartItem;
import net.n2oapp.framework.api.metadata.meta.widget.chart.AreaChart;
import net.n2oapp.framework.api.metadata.meta.widget.chart.AreaChartItem;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartLineTypeEnum;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartTypeEnum;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция компонента диаграммы-области
 */
@Component
public class AreaChartCompiler extends StandardChartCompiler<AreaChart, N2oAreaChart> {

    @Override
    public AreaChart compile(N2oAreaChart source, CompileContext<?, ?> context, CompileProcessor p) {
        AreaChart chart = new AreaChart();
        build(chart, source, p, property("n2o.api.widget.chart.area.src"));
        chart.setType(ChartTypeEnum.AREA);
        for (N2oAreaChartItem item : source.getItems()) {
            AreaChartItem component = new AreaChartItem();
            component.setFieldId(item.getFieldId());
            component.setLabel(item.getLabel());
            component.setLineType(castDefault(item.getLineType(),
                    () -> p.resolve(property("n2o.api.widget.chart.area.line_type"), ChartLineTypeEnum.class)));
            component.setColor(item.getColor());
            component.setStrokeColor(item.getStrokeColor());
            component.setHasLabel(castDefault(item.getHasLabel(),
                    () -> p.resolve(property("n2o.api.widget.chart.area.has_label"), Boolean.class)));
            chart.addItem(component);
        }
        return compileStandardChart(chart, source, p);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAreaChart.class;
    }
}
