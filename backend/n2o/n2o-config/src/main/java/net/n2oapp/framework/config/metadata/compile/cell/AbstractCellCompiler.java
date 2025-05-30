package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oBaseColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oAbstractCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oActionCell;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.cell.AbstractCell;
import net.n2oapp.framework.api.metadata.meta.cell.ActionCell;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor;

import java.util.HashMap;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.initActions;
import static net.n2oapp.framework.config.util.StylesResolver.resolveStyles;


/**
 * Компиляция абстрактной ячейки
 */
public abstract class AbstractCellCompiler<D extends AbstractCell, S extends N2oAbstractCell>
        implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    protected D build(D compiled, S source, CompileProcessor p, String defaultSrc) {
        ComponentScope columnScope = p.getScope(ComponentScope.class);
        if (columnScope != null) {
            N2oBaseColumn column = columnScope.unwrap(N2oBaseColumn.class);
            if (column != null) {
                source.setId(column.getId());
                compiled.setId(column.getId());
                compiled.setFieldKey(column.getTextFieldId());
                compiled.setTooltipFieldId(column.getTooltipFieldId());
                if (column.getContentAlignment() != null)
                    compiled.getElementAttributes().put("alignment", column.getContentAlignment().getId());
            }
        }
        compiled.setSrc(castDefault(source.getSrc(), () -> p.resolve(defaultSrc, String.class)));
        if (source.getCssClass() != null)
            compiled.getElementAttributes().put("className", p.resolveJS(source.getCssClass()));
        if (source.getStyle() != null)
            compiled.getElementAttributes().put("style", resolveStyles(source.getStyle()));
        compiled.setVisible(p.resolveJS(source.getVisible(), Boolean.class));
        compiled.setProperties(p.mapAndResolveAttributes(source));
        return compiled;
    }

    protected void compileAction(ActionCell compiled, N2oActionCell source, CompileContext<?, ?> context, CompileProcessor p) {
        source.setActions(initActions(source, p));
        Action action = ActionCompileStaticProcessor.compileAction(source, context, p, null);
        compiled.setAction(action);
    }

    protected String compileSwitch(N2oSwitch n2oSwitch, CompileProcessor p) {
        if (n2oSwitch == null) return null;
        Map<Object, String> resolvedCases = new HashMap<>();
        if (n2oSwitch.getCases() != null) {
            for (String key : n2oSwitch.getCases().keySet()) {
                resolvedCases.put(p.resolve(key), n2oSwitch.getCases().get(key));
            }
        }
        n2oSwitch.setResolvedCases(resolvedCases);
        return ScriptProcessor.buildSwitchExpression(n2oSwitch);
    }
}
