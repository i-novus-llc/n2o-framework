package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oImageUpload;
import net.n2oapp.framework.api.metadata.meta.control.ImageUpload;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция компонента загрузки изображений
 */
@Component
public class ImageUploadCompiler extends BaseFileUploadCompiler<ImageUpload, N2oImageUpload> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oImageUpload.class;
    }

    @Override
    public StandardField<ImageUpload> compile(N2oImageUpload source, CompileContext<?, ?> context, CompileProcessor p) {
        ImageUpload imageUpload = new ImageUpload();
        imageUpload.setListType(p.cast(source.getListType(),
                p.resolve(property("n2o.api.control.image_upload.list_type"), ImageUpload.ListType.class)));
        imageUpload.setLightbox(p.cast(source.getLightbox(),
                p.resolve(property("n2o.api.control.image_upload.lightbox"), Boolean.class)));
        return compileFileUpload(imageUpload, source, context, p);
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.image_upload.src";
    }

}