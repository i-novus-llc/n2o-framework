package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oHtmlWidget;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.HtmlWidget;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.StringUtils.hasLink;
import static net.n2oapp.framework.config.util.FileSystemUtil.getContentByUri;

/**
 * Компиляция html виджета
 */
@Component
public class HtmlWidgetCompiler extends BaseWidgetCompiler<HtmlWidget, N2oHtmlWidget> {

    @Override
    public HtmlWidget compile(N2oHtmlWidget source, CompileContext<?, ?> context, CompileProcessor p) {
        HtmlWidget widget = new HtmlWidget();
        N2oAbstractDatasource datasource = initDatasource(widget, source, p);
        CompiledObject object = getObject(source, datasource, p);
        compileBaseWidget(widget, source, context, p, object);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), ReduxModel.resolve, p);
        MetaActions widgetActions = initMetaActions(source, p);
        String html = p.cast(source.getHtml(), getContentByUri(source.getUrl()));
        if (html != null) {
            if (hasLink(html))
                html = html.replace("'", "\\\'");
            widget.setHtml(p.resolveJS(html.trim()));
        }
        compileToolbarAndAction(widget, source, context, p, widgetScope, widgetActions, object, null);
        return widget;
    }

    @Override
    protected N2oAbstractDatasource initDatasource(HtmlWidget compiled, N2oHtmlWidget source, CompileProcessor p) {
        N2oAbstractDatasource datasource = super.initDatasource(compiled, source, p);
        if (datasource.getSize() == null)
            datasource.setSize(1);
        return datasource;
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
