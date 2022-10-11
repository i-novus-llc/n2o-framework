package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCheckboxCell;
import net.n2oapp.framework.api.metadata.meta.cell.CheckboxCell;
import net.n2oapp.framework.api.script.ScriptProcessor;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

@Component
public class CheckboxCellCompiler extends AbstractCellCompiler<CheckboxCell, N2oCheckboxCell> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCheckboxCell.class;
    }

    @Override
    public CheckboxCell compile(N2oCheckboxCell source, CompileContext<?, ?> context, CompileProcessor p) {
        CheckboxCell cell = new CheckboxCell();
        if (source.getEnabled() != null) {
            cell.setDisabled(ScriptProcessor.invertExpression(source.getEnabled()));
        } else if (source.getActionIds() == null && source.getN2oActions() == null) {
            cell.setDisabled("true");
        }

        build(cell, source, context, p, property("n2o.api.cell.checkbox.src"));
        compileAction(cell, source, context, p);
        return cell;
    }
}
