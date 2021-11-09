package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oImageField;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.ImageStatusElement;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.control.ImageField;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция компонента вывода изображения
 */
@Component
public class ImageFieldCompiler extends ActionFieldCompiler<ImageField, N2oImageField> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oImageField.class;
    }

    @Override
    public ImageField compile(N2oImageField source, CompileContext<?, ?> context, CompileProcessor p) {
        ImageField imageField = new ImageField();
        compileField(imageField, source, context, p);
        imageField.setData(p.resolveJS(source.getData()));
        imageField.setTitle(p.resolveJS(source.getTitle()));
        imageField.setDescription(p.resolveJS(source.getDescription()));
        imageField.setTextPosition(p.cast(source.getTextPosition(),
                p.resolve(property("n2o.api.field.image_field.text_position"), TextPosition.class)));
        imageField.setWidth(p.cast(source.getWidth(), p.resolve(property("n2o.api.field.image_field.width"), String.class)));
        imageField.setShape(p.cast(source.getShape(), p.resolve(property("n2o.api.field.image_field.shape"), ImageShape.class)));
        imageField.setStatuses(compileStatuses(source.getStatuses(), p));
        compileAction(source, imageField, context, p);
        return imageField;
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
                    p.resolve(property("n2o.api.field.image.status_place"), ImageStatusElement.Place.class)));
            statusElements[i++] = statusElement;
        }
        return statusElements;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.field.image_field.src";
    }
}
