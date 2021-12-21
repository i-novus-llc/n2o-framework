package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oChart;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.chart.Chart;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция виджета диаграммы
 */
@Component
public class ChartCompiler extends BaseWidgetCompiler<Chart, N2oChart> {

    @Override
    public Chart compile(N2oChart source, CompileContext<?, ?> context, CompileProcessor p) {
        Chart chart = new Chart();
        N2oDatasource datasource = initInlineDatasource(chart, source, p);
        CompiledObject object = getObject(source, datasource, p);
        compileBaseWidget(chart, source, context, p, object);
        WidgetScope widgetScope = new WidgetScope();
        widgetScope.setWidgetId(source.getId());
        widgetScope.setClientWidgetId(chart.getId());
        widgetScope.setDatasourceId(source.getDatasourceId());

        MetaActions widgetActions = initMetaActions(source);
        compileToolbarAndAction(chart, source, context, p, widgetScope, widgetActions, object, null);

        chart.setComponent(p.compile(source.getComponent(), context, p));
        chart.getComponent().setSize(p.cast(source.getSize(), p.resolve(property("n2o.api.widget.chart.size"), Integer.class)));
        chart.getComponent().setFetchOnInit(source.getFetchOnInit());
        chart.getComponent().setWidth(source.getWidth());
        chart.getComponent().setHeight(source.getHeight());
        return chart;
    }

    @Override
    protected String getPropertyWidgetSrc() {
        return "n2o.api.widget.chart.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oChart.class;
    }
}
