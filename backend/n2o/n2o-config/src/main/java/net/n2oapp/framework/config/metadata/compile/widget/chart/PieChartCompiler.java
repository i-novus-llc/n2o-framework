package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oPieChart;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartType;
import net.n2oapp.framework.api.metadata.meta.widget.chart.PieChart;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция круговой диаграммы
 */
public class PieChartCompiler extends AbstractChartCompiler<PieChart, N2oPieChart> {

    @Override
    public PieChart compile(N2oPieChart source, CompileContext<?, ?> context, CompileProcessor p) {
        PieChart chart = new PieChart();
        build(chart, source, context, p, property("n2o.api.widget.chart.pie"));
        chart.setType(ChartType.pie);
        chart.getComponent().setCx(source.getCx());
        chart.getComponent().setCy(source.getCy());
        chart.getComponent().setInnerRadius(source.getInnerRadius());
        chart.getComponent().setOuterRadius(source.getOuterRadius());
        chart.getComponent().setStartAngle(source.getStartAngle());
        chart.getComponent().setEndAngle(source.getEndAngle());
        chart.getComponent().setNameKey(source.getNameKey());
        chart.getComponent().setDataKey(source.getDataKey());
        chart.getComponent().setColor(source.getColor());
        return chart;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPieChart.class;
    }
}
