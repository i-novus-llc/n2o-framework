package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.Layout;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oRowClick;
import net.n2oapp.framework.api.metadata.global.view.widget.table.Place;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.table.Pagination;
import net.n2oapp.framework.api.metadata.meta.widget.table.RowClick;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.util.StylesResolver;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция абстрактного спискового виджета
 */
public abstract class BaseListWidgetCompiler<D extends Widget, S extends N2oAbstractListWidget> extends BaseWidgetCompiler<D, S> {

    /**
     * Компиляция паджинации
     */
    protected Pagination compilePaging(Widget compiled, N2oAbstractListWidget source, Integer size, CompileProcessor p) {
        Pagination pagination = new Pagination();
        pagination.setSize(source.getSize() != null ? source.getSize() : size);
        if (compiled.getDataProvider() != null) compiled.getDataProvider().setSize(pagination.getSize());
        if (source.getPagination() != null) {
            pagination.setPrev(p.cast(source.getPagination().getPrev(), p.resolve(property("n2o.api.widget.list.paging.prev"), Boolean.class)));
            pagination.setNext(p.cast(source.getPagination().getNext(), p.resolve(property("n2o.api.widget.list.paging.next"), Boolean.class)));
            pagination.setFirst(p.cast(source.getPagination().getFirst(), p.resolve(property("n2o.api.widget.list.paging.first"), Boolean.class)));
            pagination.setLast(p.cast(source.getPagination().getLast(), p.resolve(property("n2o.api.widget.list.paging.last"), Boolean.class)));
            pagination.setShowSinglePage(
                    p.cast((source.getPagination().getHideSinglePage() == null ? null : !source.getPagination().getHideSinglePage()), source.getPagination().getShowSinglePage() ,
                    p.resolve(property("n2o.api.widget.list.paging.show_single_page"), Boolean.class)));
            pagination.setShowCount(p.cast(source.getPagination().getShowCount(), p.resolve(property("n2o.api.widget.list.paging.show_count"), Boolean.class)));
            pagination.setSrc(source.getPagination().getSrc());
            pagination.setLayout(p.cast(source.getPagination().getLayout(), p.resolve(property("n2o.api.widget.list.paging.layout"), Layout.class)));
            pagination.setPrevLabel(p.cast(source.getPagination().getPrevLabel(), p.resolve(property("n2o.api.widget.list.paging.prevLabel"), String.class)));
            pagination.setPrevIcon(p.cast(source.getPagination().getPrevIcon(), p.resolve(property("n2o.api.widget.list.paging.prevIcon"), String.class)));
            pagination.setNextLabel(p.cast(source.getPagination().getNextLabel(), p.resolve(property("n2o.api.widget.list.paging.nextLabel"), String.class)));
            pagination.setNextIcon(p.cast(source.getPagination().getNextIcon(), p.resolve(property("n2o.api.widget.list.paging.nextIcon"), String.class)));
            pagination.setFirstLabel(p.cast(source.getPagination().getFirstLabel(), p.resolve(property("n2o.api.widget.list.paging.firstLabel"), String.class)));
            pagination.setFirstIcon(p.cast(source.getPagination().getFirstIcon(), p.resolve(property("n2o.api.widget.list.paging.firstIcon"), String.class)));
            pagination.setLastLabel(p.cast(source.getPagination().getLastLabel(), p.resolve(property("n2o.api.widget.list.paging.lastLabel"), String.class)));
            pagination.setLastIcon(p.cast(source.getPagination().getLastIcon(), p.resolve(property("n2o.api.widget.list.paging.lastIcon"), String.class)));
            pagination.setMaxPages(p.cast(source.getPagination().getMaxPages(), p.resolve(property("n2o.api.widget.list.paging.maxPages"), Integer.class)));
            pagination.setClassName(p.cast(source.getPagination().getClassName(), p.resolve(property("n2o.api.widget.list.paging.className"), String.class)));
            pagination.setStyle(StylesResolver.resolveStyles(source.getPagination().getStyle()));
            pagination.setPlace(p.cast(source.getPagination().getPlace(), p.resolve(property("n2o.api.widget.list.paging.place"), Place.class)));
        }
        return pagination;
    }


    /**
     * Компиляция действия клика по строке
     */
    protected RowClick compileRowClick(N2oAbstractListWidget source, CompileContext<?, ?> context,
                                       CompileProcessor p, WidgetScope widgetScope,
                                       ParentRouteScope widgetRouteScope, CompiledObject object, MetaActions widgetActions) {
        RowClick rc = null;
        if (source.getRows() != null && source.getRows().getRowClick() != null) {
            N2oRowClick rowClick = source.getRows().getRowClick();
            Object enabledCondition = ScriptProcessor.resolveExpression(rowClick.getEnabled());
            if (enabledCondition == null || enabledCondition instanceof String || Boolean.TRUE.equals(enabledCondition)) {
                Action action = null;
                if (rowClick.getActionId() != null) {
                    action = widgetActions.get(rowClick.getActionId());
                } else if (rowClick.getAction() != null) {
                    action = p.compile(rowClick.getAction(), context, widgetScope,
                            widgetRouteScope, new ComponentScope(rowClick), object);
                }
                rc = new RowClick(action);
                if (action != null && StringUtils.isJs(enabledCondition)) {
                    rc.setEnablingCondition((String) ScriptProcessor.removeJsBraces(enabledCondition));
                }
            }
        }
        return rc;
    }
}
