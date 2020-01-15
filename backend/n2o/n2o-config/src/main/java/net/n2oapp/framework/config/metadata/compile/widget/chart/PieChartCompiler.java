package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oPieChart;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartType;
import net.n2oapp.framework.api.metadata.meta.widget.chart.PieChart;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция круговой диаграммы
 */
@Component
public class PieChartCompiler extends AbstractChartCompiler<PieChart, N2oPieChart>{

    @Override
    public PieChart compile(N2oPieChart source, CompileContext<?, ?> context, CompileProcessor p) {
        PieChart chart = new PieChart();
        build(chart, source, context, p, property("n2o.api.widget.chart.pie"));
        chart.setType(ChartType.pie);
        chart.getComponent().setCx(source.getCx());
        chart.getComponent().setCy(source.getCy());
        chart.getComponent().setInnerRadius(p.cast(source.getInnerRadius(), 0));
        chart.getComponent().setOuterRadius(source.getOuterRadius());
        chart.getComponent().setStartAngle(p.cast(source.getStartAngle(), 0));
        chart.getComponent().setEndAngle(p.cast(source.getEndAngle(), 360));
        chart.getComponent().setNameKey(source.getNameKey());
        chart.getComponent().setDataKey(source.getDataKey());
        chart.getComponent().setColor(source.getColor());
        chart.getComponent().setLabel(p.cast(source.getLabel(), p.resolve(property("n2o.api.default.widget.chart.label"), Boolean.class)));
        return chart;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPieChart.class;
    }
}
