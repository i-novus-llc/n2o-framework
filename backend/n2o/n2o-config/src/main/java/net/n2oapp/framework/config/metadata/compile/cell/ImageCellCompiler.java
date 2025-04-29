package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageStatusElement;
import net.n2oapp.framework.api.metadata.meta.cell.ImageCell;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElement;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElementPlaceEnum;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.StringUtils.prepareSizeAttribute;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

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
        build(cell, source, p, property("n2o.api.cell.image.src"));
        cell.setShape(castDefault(source.getShape(),
                () -> p.resolve(property("n2o.api.cell.image.shape"), ShapeTypeEnum.class)));
        cell.setWidth(prepareSizeAttribute(castDefault(source.getWidth(),
                () -> p.resolve(property("n2o.api.cell.image.width"), String.class))));

        compileAction(cell, source, context, p);

        cell.setTitle(p.resolveJS(source.getTitle()));
        cell.setDescription(p.resolveJS(source.getDescription()));
        cell.setData(p.resolveJS(source.getData()));
        cell.setTextPosition(castDefault(source.getTextPosition(),
                () -> p.resolve(property("n2o.api.cell.image.text_position"), N2oImageCell.PositionEnum.class)));
        cell.setStatuses(compileStatuses(source.getStatuses(), p));

        return cell;
    }

    private ImageStatusElement[] compileStatuses(N2oImageStatusElement[] statuses, final CompileProcessor p) {
        if (statuses == null) return null;
        int i = 0;
        ImageStatusElement[] statusElements = new ImageStatusElement[statuses.length];
        for (N2oImageStatusElement e : statuses) {
            ImageStatusElement statusElement = new ImageStatusElement();
            statusElement.setSrc(castDefault(e.getSrc(), "Status"));
            statusElement.setFieldId(e.getFieldId());
            statusElement.setIcon(p.resolveJS(e.getIcon()));
            statusElement.setPlace(castDefault(e.getPlace(),
                    () -> p.resolve(property("n2o.api.cell.image.status_place"), ImageStatusElementPlaceEnum.class)));
            statusElements[i++] = statusElement;
        }
        return statusElements;
    }

}
