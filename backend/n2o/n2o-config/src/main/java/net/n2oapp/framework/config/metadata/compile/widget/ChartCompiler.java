package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oChart;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.chart.Chart;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.StringUtils.prepareSizeAttribute;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.initMetaActions;

/**
 * Компиляция виджета диаграммы
 */
@Component
public class ChartCompiler extends BaseWidgetCompiler<Chart, N2oChart> {

    @Override
    public Chart compile(N2oChart source, CompileContext<?, ?> context, CompileProcessor p) {
        Chart chart = new Chart();
        compileBaseWidget(chart, source, context, p);
        N2oAbstractDatasource datasource = getDatasourceById(source.getDatasourceId(), p);
        CompiledObject object = getObject(source, datasource, p);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), ReduxModelEnum.RESOLVE, p);

        MetaActions widgetActions = initMetaActions(source, p);
        compileToolbarAndAction(chart, source, context, p, widgetScope, widgetActions, object, null);

        chart.setComponent(p.compile(source.getComponent(), context, p));
        chart.getComponent().setSize(castDefault(source.getSize(),
                () -> p.resolve(property("n2o.api.widget.chart.size"), Integer.class)));
        chart.getComponent().setWidth(prepareSizeAttribute(source.getWidth()));
        chart.getComponent().setHeight(prepareSizeAttribute(source.getHeight()));
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
