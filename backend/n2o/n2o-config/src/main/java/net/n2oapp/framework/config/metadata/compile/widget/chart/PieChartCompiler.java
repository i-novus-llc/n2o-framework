package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oPieChart;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartType;
import net.n2oapp.framework.api.metadata.meta.widget.chart.PieChart;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

public class PieChartCompiler extends AbstractChartCompiler<PieChart, N2oPieChart> {

    @Override
    public PieChart compile(N2oPieChart source, CompileContext<?, ?> context, CompileProcessor p) {
        PieChart chart = new PieChart();
        build(chart, source, context, p, property("n2o.api.widget.chart.pie"));
//        chart.setFetchOnInit(true);
//        chart.setWidth(source.getWidth());
//        chart.setHeight(source.getHeight());
//        chart.setType(ChartType.pie);
        chart.setCx(source.getCx());
        chart.setCy(source.getCy());
        chart.setInnerRadius(source.getInnerRadius());
        chart.setOuterRadius(source.getOuterRadius());
        chart.setStartAngle(source.getStartAngle());
        chart.setEndAngle(source.getEndAngle());
        chart.setNameKey(source.getNameKey());
        chart.setDataKey(source.getDataKey());
        chart.setColor(source.getColor());
        return chart;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPieChart.class;
    }
}
