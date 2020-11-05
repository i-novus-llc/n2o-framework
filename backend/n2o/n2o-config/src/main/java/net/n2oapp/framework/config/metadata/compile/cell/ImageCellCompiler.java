package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция ячейки с изображением
 */
@Component
public class ImageCellCompiler extends AbstractCellCompiler<N2oImageCell, N2oImageCell> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oImageCell.class;
    }

    @Override
    public N2oImageCell compile(N2oImageCell source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oImageCell cell = new N2oImageCell();
        build(cell, source, context, p, property("n2o.api.cell.image.src"));
        if (source.getShape() != null) {
            cell.setShape(source.getShape());
        }
        cell.setWidth(p.cast(source.getWidth(), p.resolve(property("n2o.api.cell.image.width"), Integer.class)));
        cell.setUrl(p.resolveJS(source.getUrl()));
        cell.setTitle(p.resolveJS(source.getTitle()));
        cell.setDescription(p.resolveJS(source.getDescription()));
        cell.setData(p.resolveJS(source.getData()));
        cell.setTextPosition(p.cast(source.getTextPosition(),
                p.resolve(property("n2o.api.cell.image.text_position"), N2oImageCell.Position.class)));
        compileAction(cell, source, context, p);
        return cell;
    }
}
