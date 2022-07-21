package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oBadgeCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.Position;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция ячейки c текстом
 */
@Component
public class BadgeCellCompiler extends AbstractCellCompiler<N2oBadgeCell, N2oBadgeCell> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oBadgeCell.class;
    }

    @Override
    public N2oBadgeCell compile(N2oBadgeCell source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oBadgeCell cell = new N2oBadgeCell();
        build(cell, source, context, p, property("n2o.api.cell.badge.src"));
        ComponentScope scope = p.getScope(ComponentScope.class);
        if (scope != null) {
            AbstractColumn column = scope.unwrap(AbstractColumn.class);
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
            cell.setImageShape(p.cast(source.getImageShape(), p.resolve(property("n2o.api.cell.badge.image_shape"), ShapeType.class)));
            cell.setImagePosition(p.cast(source.getImagePosition(), p.resolve(property("n2o.api.cell.badge.image_position"), Position.class)));
        }
        cell.setShape(p.cast(source.getShape(), p.resolve(property("n2o.api.cell.badge.shape"), ShapeType.class)));
        return cell;
    }
}
