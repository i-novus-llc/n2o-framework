package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oChartWidget;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.chart.Chart;
import net.n2oapp.framework.config.metadata.compile.PageRoutesScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;

/**
 * Компиляция виджета диаграммы
 */
public class ChartCompiler extends BaseWidgetCompiler<Chart, N2oChartWidget> {

    @Override
    public Chart compile(N2oChartWidget source, CompileContext<?, ?> context, CompileProcessor p) {
        Chart widget = new Chart();
        CompiledObject object = getObject(source, p);
        compileWidget(widget, source, context, p, object);
        ParentRouteScope widgetRoute = initWidgetRouteScope(widget, context, p);
        PageRoutesScope pageRoutesScope = p.getScope(PageRoutesScope.class);
        if (pageRoutesScope != null) {
            pageRoutesScope.put(widget.getId(), widgetRoute);
        }
        compileDataProviderAndRoutes(widget, source, context, p, null, widgetRoute, null, null, object);
        WidgetScope widgetScope = new WidgetScope();
        widgetScope.setWidgetId(source.getId());
        widgetScope.setQueryId(source.getQueryId());
        widgetScope.setClientWidgetId(widget.getId());
        MetaActions widgetActions = new MetaActions();
        compileToolbarAndAction(widget, source, context, p, widgetScope, widgetRoute, widgetActions, object, null);
        return widget;
    }

    @Override
    protected String getPropertyWidgetSrc() {
        return "n2o.api.widget.chart.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oChartWidget.class;
    }
}
