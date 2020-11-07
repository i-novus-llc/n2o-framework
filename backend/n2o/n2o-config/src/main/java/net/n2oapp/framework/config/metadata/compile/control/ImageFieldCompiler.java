package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oImageField;
import net.n2oapp.framework.api.metadata.meta.control.ImageField;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция компонента вывода изображения
 */
@Component
public class ImageFieldCompiler extends FieldCompiler<ImageField, N2oImageField> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oImageField.class;
    }

    @Override
    public ImageField compile(N2oImageField source, CompileContext<?, ?> context, CompileProcessor p) {
        ImageField imageField = new ImageField();
        compileField(imageField, source, context, p);
        imageField.setUrl(p.resolveJS(source.getUrl()));
        imageField.setData(p.resolveJS(source.getData()));
        imageField.setTitle(p.resolveJS(source.getTitle()));
        imageField.setDescription(p.resolveJS(source.getDescription()));
        imageField.setTextPosition(p.cast(source.getTextPosition(),
                p.resolve(property("n2o.api.field.image_field.text_position"), TextPosition.class)));
        imageField.setWidth(p.cast(source.getWidth(), p.resolve(property("n2o.api.field.image_field.width"), String.class)));
        return imageField;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.field.image_field.src";
    }
}
