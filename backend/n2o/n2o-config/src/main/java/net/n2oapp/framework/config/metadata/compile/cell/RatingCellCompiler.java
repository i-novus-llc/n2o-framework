package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oRatingCell;
import net.n2oapp.framework.api.metadata.meta.cell.RatingCell;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция ячейки рейтинга
 */
@Component
public class RatingCellCompiler extends AbstractCellCompiler<RatingCell, N2oRatingCell> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oRatingCell.class;
    }

    @Override
    public RatingCell compile(N2oRatingCell source, CompileContext<?, ?> context, CompileProcessor p) {
        RatingCell cell = new RatingCell();
        build(cell, source, context, p, property("n2o.api.cell.rating.src"));
        cell.setShowTooltip(castDefault(source.getShowTooltip(),
                () -> p.resolve(property("n2o.api.cell.rating.show_tooltip"), Boolean.class)));
        cell.setHalf(castDefault(source.getHalf(),
                () -> p.resolve(property("n2o.api.cell.rating.half"), Boolean.class)));
        cell.setMax(castDefault(source.getMax(),
                () -> p.resolve(property("n2o.api.cell.rating.max"), Integer.class)));
        cell.setReadonly(castDefault(source.getReadonly(),
                () -> p.resolve(property("n2o.api.cell.rating.readonly"), Boolean.class)));
        if (Boolean.FALSE.equals(source.getReadonly()))
            compileAction(cell, source, context, p);
        return cell;
    }
}
