package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.list.N2oListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oAbstractCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.ListWidget;
import net.n2oapp.framework.api.metadata.meta.widget.Rows;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

@Component
public class ListWidgetCompiler extends BaseListWidgetCompiler<ListWidget, N2oListWidget> {
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
        N2oDatasource datasource = initInlineDatasource(listWidget, source, p);
        CompiledObject object = getObject(source, datasource, p);
        compileBaseWidget(listWidget, source, context, p, object);
        WidgetScope widgetScope = new WidgetScope();
        widgetScope.setWidgetId(source.getId());
        widgetScope.setClientWidgetId(listWidget.getId());
        widgetScope.setDatasourceId(source.getDatasourceId());
        MetaActions widgetActions = initMetaActions(source);
        compileToolbarAndAction(listWidget, source, context, p, widgetScope, widgetActions, object, null);
        compileList(source, listWidget, context, widgetActions, p, widgetScope, widgetActions, object);
        if (source.getRows() != null) {
            listWidget.setRows(new Rows());
            listWidget.setRowClick(compileRowClick(source, context, p, widgetScope, object, widgetActions));
        }
        listWidget.setPaging(compilePaging(listWidget, source, p.resolve(property("n2o.api.widget.list.size"), Integer.class), p));
        return listWidget;
    }

    private void compileList(N2oListWidget source, ListWidget compiled, CompileContext<?, ?> context,
                             MetaActions actions, CompileProcessor p, WidgetScope widgetScope,
                             MetaActions widgetActions, CompiledObject object) {
        if (source.getContent() == null) return;

        Map<String, N2oAbstractCell> list = new HashMap<>();
        for (N2oListWidget.ContentElement element : source.getContent()) {
            element.setId(element.getTextFieldId());
            list.put(element.getPlace(), p.compile(p.cast(element.getCell(), new N2oTextCell()), context,
                    new ComponentScope(element), actions, widgetScope, widgetActions, object, new IndexScope()));

        }
        compiled.setList(list);
    }
}
