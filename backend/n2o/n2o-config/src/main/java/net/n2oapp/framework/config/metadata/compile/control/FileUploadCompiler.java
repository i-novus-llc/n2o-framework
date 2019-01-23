package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oFileUpload;
import net.n2oapp.framework.api.metadata.meta.control.FileUpload;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

@Component
public class FileUploadCompiler extends StandardFieldCompiler<FileUpload, N2oFileUpload> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFileUpload.class;
    }

    @Override
    public StandardField<FileUpload> compile(N2oFileUpload source, CompileContext<?, ?> context, CompileProcessor p) {
        FileUpload compiled = new FileUpload();
        compiled.setUploadUrl(p.resolveJS(source.getUploadUrl()));
        compiled.setDeleteUrl(p.resolveJS(source.getDeleteUrl()));
        compiled.setAjax(p.cast(source.getMulti(), true));
        compiled.setMulti(p.cast(source.getMulti(), false));
        compiled.setShowSize(p.cast(source.getShowSize(),
                p.resolve(property("n2o.api.control.fileupload.show_size"), Boolean.class)));
        compiled.setValueFieldId(p.cast(source.getValueFieldId(),
                p.resolve(property("n2o.api.control.fileupload.value_field_id"), String.class)));
        compiled.setLabelFieldId(p.cast(source.getLabelFieldId(),
                p.resolve(property("n2o.api.control.fileupload.label_field_id"), String.class)));
        compiled.setUrlFieldId(p.cast(source.getUrlFieldId(),
                p.resolve(property("n2o.api.control.fileupload.url_field_id"), String.class)));
        compiled.setResponseFieldId(p.cast(source.getMessageFieldId(),
                p.resolve(property("n2o.api.control.fileupload.response_field_id"), String.class)));
        compiled.setRequestParam(p.cast(source.getRequestParam(), "file"));
        return compileStandardField(compiled, source, context, p);
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.fileupload.src";
    }
}
