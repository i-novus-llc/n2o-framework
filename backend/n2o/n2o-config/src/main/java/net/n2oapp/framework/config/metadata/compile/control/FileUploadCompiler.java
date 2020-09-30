package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oFileUpload;
import net.n2oapp.framework.api.metadata.meta.control.FileUpload;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

/**
 * Компиляция компонента загрузки файлов
 */
@Component
public class FileUploadCompiler extends BaseFileUploadCompiler<FileUpload, N2oFileUpload> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFileUpload.class;
    }

    @Override
    public StandardField<FileUpload> compile(N2oFileUpload source, CompileContext<?, ?> context, CompileProcessor p) {
        return compileFileUpload(new FileUpload(), source, context, p);
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.file_upload.src";
    }
}
