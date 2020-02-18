package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oIconCell;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция ячейки с иконкой
 */
@Component
public class IconCellCompiler extends AbstractCellCompiler<N2oIconCell, N2oIconCell> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oIconCell.class;
    }

    @Override
    public N2oIconCell compile(N2oIconCell source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oIconCell cell = new N2oIconCell();
        build(cell, source, context, p, property("n2o.api.cell.icon.src"));
        cell.setText(source.getText());
        cell.setIconType(p.cast(source.getIconType(), IconType.icon));
        cell.setIcon(p.cast(source.getIcon(), compileSwitch(source.getIconSwitch(), p)));
        if (source.getPosition() != null) {
            cell.setPosition(source.getPosition());
        }
        return cell;
    }
}
