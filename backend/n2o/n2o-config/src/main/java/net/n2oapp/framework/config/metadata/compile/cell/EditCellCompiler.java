package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.EditType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oEditCell;
import net.n2oapp.framework.api.metadata.meta.control.EditCell;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция редактируемой ячейки таблицы
 */
@Component
public class EditCellCompiler extends AbstractCellCompiler<EditCell, N2oEditCell> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oEditCell.class;
    }

    @Override
    public EditCell compile(N2oEditCell source, CompileContext<?, ?> context, CompileProcessor p) {
        EditCell cell = new EditCell();
        build(cell, source, context, p, property("n2o.default.cell.edit.src"));
        compileAction(cell, source, context, p);

        if (source.getN2oField() != null) {
            net.n2oapp.framework.api.metadata.Component component = p.compile(source.getN2oField(), context);
            if (component instanceof StandardField) {
                cell.setControl(((StandardField) component).getControl());
            } else {
                cell.setControl(component);
            }
        }

        cell.setFormat(source.getFormat());
        cell.setEditType(source.getEditType() == null ? EditType.inline : source.getEditType());
        cell.setEnabled(p.cast(p.resolveJS(source.getEnabled(), Boolean.class), true));
        return cell;
    }
}
