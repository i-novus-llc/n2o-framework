package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.EditType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oEditCell;
import net.n2oapp.framework.api.metadata.meta.cell.EditCell;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
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
        build(cell, source, context, p, property("n2o.api.cell.edit.src"));
        compileAction(cell, source, context, p);

        if (source.getN2oField() != null) {
            Field control = p.compile(source.getN2oField(), context);
            if (control instanceof StandardField) {
                cell.setControl(((StandardField) control).getControl());
            } else {
                cell.setControl(control);
            }
            ComponentScope columnScope = p.getScope(ComponentScope.class);
            AbstractColumn column = null;
            if (columnScope != null)
                column = columnScope.unwrap(AbstractColumn.class);
            cell.setEditFieldId(p.cast(control.getId(), column != null ? column.getTextFieldId() : null));
        }

        cell.setFormat(source.getFormat());
        cell.setEditType(p.cast(source.getEditType(),
                p.resolve(property("n2o.api.cell.edit.type"), EditType.class)));
        cell.setEnabled(p.cast(p.resolveJS(source.getEnabled(), Boolean.class),
                p.resolve(property("n2o.api.cell.edit.enabled"), Boolean.class)));
        return cell;
    }
}
