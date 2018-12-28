package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция ячейки картинка
 */
@Component
public class ImageCellCompiler extends AbstractCellCompiler<N2oImageCell, N2oImageCell> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oImageCell.class;
    }


    @Override
    public N2oImageCell compile(N2oImageCell source, CompileContext<?,?> context, CompileProcessor p) {
        N2oImageCell cell = new N2oImageCell();
        build(cell, source, context, p, property("n2o.default.cell.image.src"));
        if(source.getShape() != null){
            cell.setShape(source.getShape());
        }
        compileAction(cell, source, context, p);
        return cell;
    }
}
