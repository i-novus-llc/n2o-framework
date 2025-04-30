package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCheckboxCell;
import net.n2oapp.framework.api.metadata.meta.cell.CheckboxCell;
import net.n2oapp.framework.api.script.ScriptProcessor;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

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
        } else if (source.getActionId() == null && isEmpty(source.getActions())) {
            cell.setDisabled("true");
        }

        build(cell, source, p, property("n2o.api.cell.checkbox.src"));
        compileAction(cell, source, context, p);
        return cell;
    }
}
