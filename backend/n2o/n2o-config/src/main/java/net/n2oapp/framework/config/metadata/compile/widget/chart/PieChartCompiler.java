package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oPieChart;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartTypeEnum;
import net.n2oapp.framework.api.metadata.meta.widget.chart.PieChart;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция круговой диаграммы
 */
@Component
public class PieChartCompiler extends AbstractChartCompiler<PieChart, N2oPieChart>{

    @Override
    public PieChart compile(N2oPieChart source, CompileContext<?, ?> context, CompileProcessor p) {
        PieChart chart = new PieChart();
        build(chart, source, p, property("n2o.api.widget.chart.pie.src"));
        chart.setType(ChartTypeEnum.pie);
        chart.getComponent().setCenterX(source.getCenterX());
        chart.getComponent().setCenterY(source.getCenterY());
        chart.getComponent().setInnerRadius(castDefault(source.getInnerRadius(),
                () -> p.resolve(property("n2o.api.widget.chart.pie.inner_radius"), Integer.class)));
        chart.getComponent().setOuterRadius(source.getOuterRadius());
        chart.getComponent().setStartAngle(castDefault(source.getStartAngle(),
                () -> p.resolve(property("n2o.api.widget.chart.pie.start_angle"), Integer.class)));
        chart.getComponent().setEndAngle(castDefault(source.getEndAngle(),
                () -> p.resolve(property("n2o.api.widget.chart.pie.end_angle"), Integer.class)));
        chart.getComponent().setNameFieldId(source.getNameFieldId());
        chart.getComponent().setValueFieldId(source.getValueFieldId());
        chart.getComponent().setTooltipFieldId(source.getTooltipFieldId());
        chart.getComponent().setColor(source.getColor());
        chart.getComponent().setHasLabel(castDefault(source.getHasLabel(),
                () -> p.resolve(property("n2o.api.widget.chart.pie.has_label"), Boolean.class)));
        return chart;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPieChart.class;
    }
}
