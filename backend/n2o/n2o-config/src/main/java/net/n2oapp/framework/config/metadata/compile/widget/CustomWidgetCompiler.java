package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oCustomWidget;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.CustomWidget;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.initMetaActions;

@Component
public class CustomWidgetCompiler extends BaseWidgetCompiler<CustomWidget, N2oCustomWidget> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCustomWidget.class;
    }

    @Override
    protected String getPropertyWidgetSrc() {
        return null;
    }

    @Override
    public CustomWidget compile(N2oCustomWidget source, CompileContext<?, ?> context, CompileProcessor p) {
        CustomWidget widget = new CustomWidget();
        compileBaseWidget(widget, source, context, p);
        N2oAbstractDatasource datasource = getDatasourceById(source.getDatasourceId(), p);
        CompiledObject object = getObject(source, datasource, p);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), ReduxModelEnum.resolve, p);
        MetaActions widgetActions = initMetaActions(source, p);
        compileToolbarAndAction(widget, source, context, p, widgetScope, widgetActions, object, null);
        return widget;
    }
}
