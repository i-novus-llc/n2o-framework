package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oIconCell;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.cell.IconCell;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция ячейки с иконкой
 */
@Component
public class IconCellCompiler extends AbstractCellCompiler<IconCell, N2oIconCell> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oIconCell.class;
    }

    @Override
    public IconCell compile(N2oIconCell source, CompileContext<?, ?> context, CompileProcessor p) {
        IconCell cell = new IconCell();
        build(cell, source, context, p, property("n2o.api.cell.icon.src"));
        cell.setText(source.getText());
        cell.setIcon(castDefault(source.getIcon(), () -> compileSwitch(source.getIconSwitch(), p)));
        cell.setPosition(castDefault(source.getPosition(),
                () -> p.resolve(property("n2o.api.cell.icon.position"), PositionEnum.class)));

        return cell;
    }
}
