package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oPagination;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oRowClick;
import net.n2oapp.framework.api.metadata.global.view.widget.table.Place;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShowCountType;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.table.Pagination;
import net.n2oapp.framework.api.metadata.meta.widget.table.RowClick;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.util.StylesResolver;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.compileAction;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.initActions;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

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
        N2oPagination sourcePagination = source.getPagination() != null ? source.getPagination() : new N2oPagination();
        pagination.setPrev(p.cast(sourcePagination.getPrev(), p.resolve(property("n2o.api.widget.list.paging.prev"), Boolean.class)));
        pagination.setNext(p.cast(sourcePagination.getNext(), p.resolve(property("n2o.api.widget.list.paging.next"), Boolean.class)));
        pagination.setShowLast(p.cast(sourcePagination.getShowLast(), p.resolve(property("n2o.api.widget.list.paging.show_last"), Boolean.class)));
        pagination.setShowCount(p.cast(sourcePagination.getShowCount(), pagination.getShowLast() ? ShowCountType.ALWAYS : ShowCountType.NEVER));
        pagination.setSrc(sourcePagination.getSrc());
        pagination.setPrevLabel(p.cast(sourcePagination.getPrevLabel(), p.resolve(property("n2o.api.widget.list.paging.prevLabel"), String.class)));
        pagination.setPrevIcon(p.cast(sourcePagination.getPrevIcon(), p.resolve(property("n2o.api.widget.list.paging.prevIcon"), String.class)));
        pagination.setNextLabel(p.cast(sourcePagination.getNextLabel(), p.resolve(property("n2o.api.widget.list.paging.nextLabel"), String.class)));
        pagination.setNextIcon(p.cast(sourcePagination.getNextIcon(), p.resolve(property("n2o.api.widget.list.paging.nextIcon"), String.class)));
        pagination.setClassName(p.cast(sourcePagination.getClassName(), p.resolve(property("n2o.api.widget.list.paging.className"), String.class)));
        pagination.setStyle(StylesResolver.resolveStyles(sourcePagination.getStyle()));
        pagination.setPlace(p.cast(sourcePagination.getPlace(), p.resolve(property("n2o.api.widget.list.paging.place"), Place.class)));
        return pagination;
    }


    /**
     * Компиляция действия клика по строке
     */
    protected RowClick compileRowClick(N2oAbstractListWidget source, CompileContext<?, ?> context,
                                       CompileProcessor p, WidgetScope widgetScope,
                                       CompiledObject object, MetaActions widgetActions) {
        RowClick rc = null;
        if (source.getRows() != null && source.getRows().getRowClick() != null) {
            N2oRowClick rowClick = source.getRows().getRowClick();
            Object enabledCondition = ScriptProcessor.resolveExpression(rowClick.getEnabled());
            if (enabledCondition == null || enabledCondition instanceof String || Boolean.TRUE.equals(enabledCondition)) {
                rowClick.setActions(initActions(rowClick, p));
                if (isNotEmpty(rowClick.getActions())) {
                    Action compiledAction = compileAction(rowClick, context, p, object, widgetScope);
                    rc = new RowClick(compiledAction);
                    rc.setProperties(p.mapAttributes(rowClick));
                    if (compiledAction != null && StringUtils.isJs(enabledCondition)) {
                        rc.setEnablingCondition((String) ScriptProcessor.removeJsBraces(enabledCondition));
                    }
                }
            }
        }
        return rc;
    }
}
