package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oFileUpload;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.control.FileUpload;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;

import java.util.HashMap;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция базового компонента загрузки файлов
 */
public abstract class BaseFileUploadCompiler<D extends FileUpload, S extends N2oFileUpload> extends StandardFieldCompiler<D, S> {

    protected StandardField<D> compileFileUpload(D control, S source, CompileContext<?, ?> context, CompileProcessor p) {
        control.setUploadUrl(p.resolveJS(source.getUploadUrl()));
        control.setDeleteUrl(p.resolveJS(source.getDeleteUrl()));
        control.setAjax(p.cast(source.getAjax(), true));
        control.setMulti(p.cast(source.getMulti(), false));
        control.setAccept(source.getAccept());
        control.setShowSize(p.cast(source.getShowSize(),
                () -> p.resolve(property("n2o.api.control.file_upload.show_size"), Boolean.class)));
        control.setValueFieldId(p.cast(source.getValueFieldId(),
                () -> p.resolve(property("n2o.api.control.file_upload.value_field_id"), String.class)));
        control.setLabelFieldId(p.cast(source.getLabelFieldId(),
                () -> p.resolve(property("n2o.api.control.file_upload.label_field_id"), String.class)));
        control.setUrlFieldId(p.cast(source.getUrlFieldId(),
                () -> p.resolve(property("n2o.api.control.file_upload.url_field_id"), String.class)));
        control.setResponseFieldId(p.cast(source.getMessageFieldId(),
                () -> p.resolve(property("n2o.api.control.file_upload.response_field_id"), String.class)));
        control.setRequestParam(p.cast(source.getRequestParam(), "file"));
        return compileStandardField(control, source, context, p);
    }

    @Override
    protected Object compileDefValues(N2oFileUpload source, CompileProcessor p) {
        if (source.getDefValue() == null) {
            return null;
        }
        DefaultValues values = new DefaultValues();
        values.setValues(new HashMap<>());
        source.getDefValue().forEach((f, v) -> values.getValues().put(f, p.resolve(v)));
        return values;
    }
}
