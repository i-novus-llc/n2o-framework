package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oRowClick;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.table.Pagination;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;

/**
 * Компиляция абстрактного спискового виджета
 */
public abstract class BaseListWidgetCompiler<D extends Widget, S extends N2oAbstractListWidget> extends BaseWidgetCompiler<D, S> {

    /**
     * Компиляция паджинации
     */
    protected Pagination compilePaging(N2oAbstractListWidget source, Integer size) {
        Boolean prev = null;
        Boolean next = null;
        if (source.getPagination() != null) {
            prev = source.getPagination().getPrev();
            next = source.getPagination().getNext();
        }
        Pagination pagination = new Pagination();
        pagination.setSize(source.getSize() != null ? source.getSize() : size);
        pagination.setPrev(prev);
        pagination.setNext(next);
        return pagination;
    }


    /**
     * Компиляция действия клика по строке
     */
    protected Action compileRowClick(N2oAbstractListWidget source, CompileContext<?, ?> context,
                                     CompileProcessor p, WidgetScope widgetScope, ParentRouteScope widgetRouteScope, CompiledObject object) {
        AbstractAction action = null;
        if (source.getRows() != null && source.getRows().getRowClick() != null) {
            N2oRowClick rowClick = source.getRows().getRowClick();
            Object enabledCondition = ScriptProcessor.resolveExpression(rowClick.getEnabled());
            if (enabledCondition == null || enabledCondition instanceof String || Boolean.TRUE.equals(enabledCondition)) {
                if (rowClick.getActionId() != null) {
                    MetaActions actions = p.getScope(MetaActions.class);
                    action = (AbstractAction) actions.get(rowClick.getActionId());
                } else if (rowClick.getAction() != null) {
                    action = p.compile(rowClick.getAction(), context, widgetScope,
                            widgetRouteScope, new ComponentScope(rowClick), object);
                }
                if (action != null && StringUtils.isJs(enabledCondition))
                    action.setEnablingCondition((String) ScriptProcessor.removeJsBraces(enabledCondition));
            }
        }
        return action;
    }
}
