package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.ImageStatusElement;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
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
        cell.setShape(p.cast(source.getShape(), p.resolve(property("n2o.api.cell.image.shape"), ImageShape.class)));
        cell.setWidth(p.cast(source.getWidth(), p.resolve(property("n2o.api.cell.image.width"), Integer.class)));

        compileAction(cell, source, context, p);
        if (cell.getCompiledAction() != null && cell.getCompiledAction() instanceof LinkAction) {
            LinkAction linkAction = ((LinkAction) cell.getCompiledAction());
            cell.setActionId(null);
            cell.setUrl(linkAction.getUrl());
            cell.setTarget(linkAction.getTarget());
            cell.setPathMapping(linkAction.getPathMapping());
            cell.setQueryMapping(linkAction.getQueryMapping());
        }

        cell.setTitle(p.resolveJS(source.getTitle()));
        cell.setDescription(p.resolveJS(source.getDescription()));
        cell.setData(p.resolveJS(source.getData()));
        cell.setTextPosition(p.cast(source.getTextPosition(),
                p.resolve(property("n2o.api.cell.image.text_position"), N2oImageCell.Position.class)));
        cell.setStatuses(compileStatuses(source.getStatuses(), p));

        return cell;
    }

    private ImageStatusElement[] compileStatuses(ImageStatusElement[] statuses, final CompileProcessor p) {
        if (statuses == null) return null;
        int i = 0;
        ImageStatusElement[] statusElements = new ImageStatusElement[statuses.length];
        for (ImageStatusElement e : statuses) {
            ImageStatusElement statusElement = new ImageStatusElement();
            statusElement.setSrc(p.cast(e.getSrc(), "Status"));
            statusElement.setFieldId(e.getFieldId());
            statusElement.setIcon(p.resolveJS(e.getIcon()));
            statusElement.setPlace(p.cast(e.getPlace(),
                    p.resolve(property("n2o.api.cell.image.status_place"), ImageStatusElement.Place.class)));
            statusElements[i++] = statusElement;
        }
        return statusElements;
    }

}
