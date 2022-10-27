package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.meta.badge.Position;
import net.n2oapp.framework.api.metadata.meta.cell.TextCell;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция ячейки с текстом
 */
@Component
public class TextCellCompiler extends AbstractCellCompiler<TextCell, N2oTextCell> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTextCell.class;
    }

    @Override
    public TextCell compile(N2oTextCell source, CompileContext<?, ?> context, CompileProcessor p) {
        TextCell cell = new TextCell();
        build(cell, source, context, p, property("n2o.api.cell.text.src"));
        if (source.getClassSwitch() != null)
            cell.setCssClass(p.cast(source.getCssClass(), compileSwitch(source.getClassSwitch(), p)));
        cell.setFormat(source.getFormat());
        cell.setSubTextFieldKey(source.getSubTextFieldKey());
        cell.setSubTextFormat(source.getSubTextFormat());
        cell.setIcon(source.getIcon());
        cell.setIconPosition(p.cast(source.getIconPosition(), Position.left));
        return cell;
    }
}
