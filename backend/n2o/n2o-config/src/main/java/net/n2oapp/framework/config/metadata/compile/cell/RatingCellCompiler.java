package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oRatingCell;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция ячейки рейтинга
 */
@Component
public class RatingCellCompiler extends AbstractCellCompiler<N2oRatingCell, N2oRatingCell> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oRatingCell.class;
    }

    @Override
    public N2oRatingCell compile(N2oRatingCell source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oRatingCell cell = new N2oRatingCell();
        build(cell, source, context, p, property("n2o.api.cell.rating.src"));
        cell.setShowTooltip(p.cast(source.getShowTooltip(), p.resolve(property("n2o.api.cell.rating.showTooltip"), Boolean.class)));
        cell.setHalf(p.cast(source.getHalf(), p.resolve(property("n2o.api.cell.rating.half"), Boolean.class)));
        cell.setMax(p.cast(source.getMax(), p.resolve(property("n2o.api.cell.rating.max"), Integer.class)));
        cell.setReadonly(p.cast(source.getReadonly(), p.resolve(property("n2o.api.cell.rating.readonly"), Boolean.class)));
        if (Boolean.FALSE.equals(source.getReadonly())) {
            if (source.getActionId() != null) {
                MetaActions metaActions = p.getScope(MetaActions.class);
                if (metaActions != null && !(metaActions.get(source.getActionId()) instanceof InvokeAction))
                    throw new N2oException("Rating cell supports only invoke action");
            }
            compileAction(cell, source, context, p);
        }
        return cell;
    }
}
