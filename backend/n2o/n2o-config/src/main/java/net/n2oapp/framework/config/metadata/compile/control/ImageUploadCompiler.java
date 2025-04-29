package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oImageUpload;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.control.ImageUpload;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.StringUtils.prepareSizeAttribute;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

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
        imageUpload.setListType(castDefault(source.getListType(),
                () -> p.resolve(property("n2o.api.control.image_upload.list_type"), ImageUpload.ListTypeEnum.class)));
        imageUpload.setCanLightbox(castDefault(source.getCanLightbox(),
                () -> p.resolve(property("n2o.api.control.image_upload.can_lightbox"), Boolean.class)));
        imageUpload.setCanDelete(castDefault(source.getCanDelete(),
                () -> p.resolve(property("n2o.api.control.image_upload.can_delete"), Boolean.class)));
        imageUpload.setWidth(prepareSizeAttribute(castDefault(source.getWidth(),
                () -> p.resolve(property("n2o.api.control.image_upload.width"), String.class))));
        imageUpload.setHeight(prepareSizeAttribute(castDefault(source.getHeight(),
                () -> p.resolve(property("n2o.api.control.image_upload.height"), String.class))));
        imageUpload.setIcon(source.getIcon());
        imageUpload.setIconSize(prepareSizeAttribute(source.getIconSize()));
        imageUpload.setShowTooltip(castDefault(source.getShowTooltip(),
                () -> p.resolve(property("n2o.api.control.image_upload.show_tooltip"), Boolean.class)));
        imageUpload.setShape(castDefault(source.getShape(),
                () -> p.resolve(property("n2o.api.control.image_upload.shape"), ShapeTypeEnum.class)));
        imageUpload.setShowName(castDefault(source.getShowName(),
                () -> p.resolve(property("n2o.api.control.image_upload.show_name"), Boolean.class)));
        return compileFileUpload(imageUpload, source, context, p);
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.image_upload.src";
    }

}