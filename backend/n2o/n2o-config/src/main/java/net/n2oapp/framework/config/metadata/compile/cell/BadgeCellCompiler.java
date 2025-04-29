package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oBaseColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oBadgeCell;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.cell.BadgeCell;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция ячейки c текстом
 */
@Component
public class BadgeCellCompiler extends AbstractCellCompiler<BadgeCell, N2oBadgeCell> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oBadgeCell.class;
    }

    @Override
    public BadgeCell compile(N2oBadgeCell source, CompileContext<?, ?> context, CompileProcessor p) {
        BadgeCell cell = new BadgeCell();
        build(cell, source, context, p, property("n2o.api.cell.badge.src"));
        ComponentScope scope = p.getScope(ComponentScope.class);
        if (scope != null) {
            N2oBaseColumn column = scope.unwrap(N2oBaseColumn.class);
            if (column != null)
                cell.setId(column.getId());
        }
        if (source.getPosition() != null) {
            cell.setPosition(source.getPosition());
        }
        if (source.getText() != null) {
            cell.setText(p.resolveJS(source.getText()));
            cell.setTextFormat(source.getTextFormat());
        }

        if (source.getN2oSwitch() != null) {
            cell.setColor(compileSwitch(source.getN2oSwitch(), p));
        }
        else
            cell.setColor(p.resolveJS(source.getColor()));

        if (source.getFormat() != null)
            cell.setFormat(source.getFormat());
        if (source.getImageFieldId() != null) {
            cell.setImageFieldId(source.getImageFieldId());
            cell.setImageShape(castDefault(source.getImageShape(), () -> p.resolve(property("n2o.api.cell.badge.image_shape"), ShapeTypeEnum.class)));
            cell.setImagePosition(castDefault(source.getImagePosition(), () -> p.resolve(property("n2o.api.cell.badge.image_position"), PositionEnum.class)));
        }
        cell.setShape(castDefault(source.getShape(), () -> p.resolve(property("n2o.api.cell.badge.shape"), ShapeTypeEnum.class)));
        return cell;
    }
}
