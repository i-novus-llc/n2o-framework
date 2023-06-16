package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oSwitchCell;
import net.n2oapp.framework.api.metadata.meta.cell.AbstractCell;
import net.n2oapp.framework.api.metadata.meta.cell.SwitchCell;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция переключателя ячеек
 */
@Component
public class SwitchCellCompiler implements BaseSourceCompiler<SwitchCell, N2oSwitchCell, CompileContext<?, ?>> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSwitchCell.class;
    }

    @Override
    public SwitchCell compile(N2oSwitchCell source,
                              CompileContext<?, ?> context,
                              CompileProcessor p) {
        SwitchCell cell = new SwitchCell();
        cell.setSrc(p.resolve(property("n2o.api.cell.switch.src"), String.class));
        cell.setSwitchFieldId(source.getValueFieldId());

        ComponentScope scope = p.getScope(ComponentScope.class);
        if (scope != null) {
            AbstractColumn column = scope.unwrap(AbstractColumn.class);
            if (column != null) {
                cell.setId(column.getId());
            }
        }
        initCases(cell, source, context, p);
        initDefaultCase(cell, source, context, p);

        return cell;
    }
    
    private void initDefaultCase(SwitchCell cell,
                                 N2oSwitchCell source,
                                 CompileContext<?, ?> context,
                                 CompileProcessor p) {
        if (source.getDefaultCase() != null)
            cell.setSwitchDefault(p.compile(source.getDefaultCase(), context, p));
    }
    
    private void initCases(SwitchCell cell,
                           N2oSwitchCell source,
                           CompileContext<?, ?> context,
                           CompileProcessor p) {
        for (var c : source.getCases()) {
            AbstractCell compile = p.compile(c.getItem(), context, p);
            cell.getSwitchList().put(c.getValue(), compile);
        }
    }
}
