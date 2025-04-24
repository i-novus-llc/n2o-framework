package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oEditCell;
import net.n2oapp.framework.api.metadata.meta.cell.EditCell;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

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
            if (control instanceof StandardField field) {
                ComponentScope columnScope = p.getScope(ComponentScope.class);
                String columnTextFieldId = null;
                if (columnScope != null) {
                    AbstractColumn column = columnScope.unwrap(AbstractColumn.class);
                    if (column != null) {
                        columnTextFieldId = column.getTextFieldId();
                    }
                }
                if (field.getControl().getId() == null)
                    field.getControl().setId(columnTextFieldId);
                cell.setControl(field.getControl());
            } else {
                cell.setControl(control);
            }
        }
        cell.setFormat(source.getFormat());
        cell.setEnabled(castDefault(
                p.resolveJS(source.getEnabled(), Boolean.class),
                () -> p.resolve(property("n2o.api.cell.edit.enabled"), Boolean.class)));
        return cell;
    }
}