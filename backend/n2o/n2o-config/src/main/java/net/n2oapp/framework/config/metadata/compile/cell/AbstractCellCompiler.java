package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oAbstractCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oActionCell;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;


/**
 * Компиляция абстрактной ячейки
 */
public abstract class AbstractCellCompiler<D extends N2oAbstractCell, S extends N2oAbstractCell>
        implements BaseSourceCompiler<D, S, CompileContext<?,?>> {

    protected D build(D compiled, S source, CompileContext<?,?> context, CompileProcessor p, String defaultSrc) {
        ComponentScope columnScope = p.getScope(ComponentScope.class);
        if (columnScope != null) {
            AbstractColumn column = columnScope.unwrap(AbstractColumn.class);
            source.setId(column.getId());
            compiled.setId(column.getId());
            compiled.setFieldKey(column.getTextFieldId());
        }
        compiled.setSrc(p.cast(source.getSrc(), p.resolve(defaultSrc, String.class)));
        compiled.setCssClass(p.resolveJS(source.getCssClass()));
        return compiled;
    }

    protected void compileAction(N2oActionCell compiled, N2oActionCell source, CompileContext<?,?> context, CompileProcessor p) {
        if (source.getActionId() != null || source.getAction() != null) {
            Action action;
            MetaActions actions = p.getScope(MetaActions.class);
            if (source.getActionId() != null) {
                action = actions.get(source.getActionId());
            } else {
                action = p.compile(source.getAction(), context, new ComponentScope(source));
            }
            compiled.setActionId(action.getId());
            compiled.setCompiledAction(action);
        }
    }


}
