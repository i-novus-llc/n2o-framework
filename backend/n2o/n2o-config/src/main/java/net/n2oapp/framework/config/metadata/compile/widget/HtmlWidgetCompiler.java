package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oHtmlWidget;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.HtmlWidget;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.util.FileSystemUtil.getContentByUri;

/**
 * Компиляция html виджета
 */
@Component
public class HtmlWidgetCompiler extends BaseWidgetCompiler<HtmlWidget, N2oHtmlWidget> {

    @Override
    public HtmlWidget compile(N2oHtmlWidget source, CompileContext<?, ?> context, CompileProcessor p) {
        HtmlWidget widget = new HtmlWidget();
        copyInlineDatasource(widget, source, p);
        CompiledObject object = getObject(source, p);
        compileWidget(widget, source, context, p, object);
        compileDataProviderAndRoutes(widget, source, context, p, null,null, null, object);
        WidgetScope widgetScope = new WidgetScope();
        widgetScope.setClientWidgetId(widget.getId());
        widgetScope.setOldRoute(p.cast(source.getRoute(), source.getId()));
        widgetScope.setWidgetId(source.getId());
        MetaActions widgetActions = initMetaActions(source);
        if (source.getSrc() != null)
            widget.setHtml(getContentByUri(source.getSrc()));
        else if (source.getHtml() != null)
            widget.setHtml(source.getHtml().trim());
        compileToolbarAndAction(widget, source, context, p, widgetScope, widgetActions, object, null);
        return widget;
    }

    @Override
    protected String getPropertyWidgetSrc() {
        return "n2o.api.widget.html.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oHtmlWidget.class;
    }
}
