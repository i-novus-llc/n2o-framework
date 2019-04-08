package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.list.N2oListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oAbstractCell;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.ListWidget;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.PageRoutesScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ListWidgetCompiler extends BaseWidgetCompiler<ListWidget, N2oListWidget> {
    @Override
    protected String getPropertyWidgetSrc() {
        return "n2o.api.widget.list.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oListWidget.class;
    }

    @Override
    public ListWidget compile(N2oListWidget source, CompileContext<?, ?> context, CompileProcessor p) {
        ListWidget listWidget = new ListWidget();
        CompiledObject object = getObject(source, p);
        compileWidget(listWidget, source, context, p, object);
        ParentRouteScope widgetRoute = initWidgetRouteScope(listWidget, context, p);
        PageRoutesScope pageRoutesScope = p.getScope(PageRoutesScope.class);
        if (pageRoutesScope != null) {
            pageRoutesScope.put(listWidget.getId(), widgetRoute);
        }
        compileDataProviderAndRoutes(listWidget, source, p, null, widgetRoute, null, null);
        WidgetScope widgetScope = new WidgetScope();
        widgetScope.setWidgetId(source.getId());
        widgetScope.setQueryId(source.getQueryId());
        widgetScope.setClientWidgetId(listWidget.getId());
        MetaActions widgetActions = new MetaActions();
        compileToolbarAndAction(listWidget, source, context, p, widgetScope, widgetRoute, widgetActions, object, null);
        compileList(source, listWidget, context, widgetActions, p);
        Boolean prev = null;
        Boolean next = null;
        if (source.getPagination() != null) {
            prev = source.getPagination().getPrev();
            next = source.getPagination().getNext();
        }
        listWidget.setPaging(createPaging(source.getSize(), prev, next, "n2o.api.default.widget.list.size", p));
        return listWidget;
    }

    private void compileList(N2oListWidget source, ListWidget compiled, CompileContext<?, ?> context, MetaActions actions, CompileProcessor p) {
        if (source.getContent() == null) return;

        Map<String, N2oAbstractCell> list = new HashMap<>();
        for (N2oListWidget.ContentElement element : source.getContent()) {
            list.put(element.getPlace(), p.compile(element.getCell(), context, new ComponentScope(element), actions, new IndexScope()));
        }
        compiled.setList(list);
    }
}
