package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageStatusElement;
import net.n2oapp.framework.api.metadata.meta.cell.ImageCell;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElement;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElementPlace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция ячейки с изображением
 */
@Component
public class ImageCellCompiler extends AbstractCellCompiler<ImageCell, N2oImageCell> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oImageCell.class;
    }

    @Override
    public ImageCell compile(N2oImageCell source, CompileContext<?, ?> context, CompileProcessor p) {
        ImageCell cell = new ImageCell();
        build(cell, source, context, p, property("n2o.api.cell.image.src"));
        cell.setShape(p.cast(source.getShape(), p.resolve(property("n2o.api.cell.image.shape"), ShapeType.class)));
        cell.setWidth(p.cast(source.getWidth(), p.resolve(property("n2o.api.cell.image.width"), String.class)));

        compileAction(cell, source, context, p);

        cell.setTitle(p.resolveJS(source.getTitle()));
        cell.setDescription(p.resolveJS(source.getDescription()));
        cell.setData(p.resolveJS(source.getData()));
        cell.setTextPosition(p.cast(source.getTextPosition(),
                p.resolve(property("n2o.api.cell.image.text_position"), N2oImageCell.Position.class)));
        cell.setStatuses(compileStatuses(source.getStatuses(), p));

        return cell;
    }

    private ImageStatusElement[] compileStatuses(N2oImageStatusElement[] statuses, final CompileProcessor p) {
        if (statuses == null) return null;
        int i = 0;
        ImageStatusElement[] statusElements = new ImageStatusElement[statuses.length];
        for (N2oImageStatusElement e : statuses) {
            ImageStatusElement statusElement = new ImageStatusElement();
            statusElement.setSrc(p.cast(e.getSrc(), "Status"));
            statusElement.setFieldId(e.getFieldId());
            statusElement.setIcon(p.resolveJS(e.getIcon()));
            statusElement.setPlace(p.cast(e.getPlace(),
                    p.resolve(property("n2o.api.cell.image.status_place"), ImageStatusElementPlace.class)));
            statusElements[i++] = statusElement;
        }
        return statusElements;
    }

}
