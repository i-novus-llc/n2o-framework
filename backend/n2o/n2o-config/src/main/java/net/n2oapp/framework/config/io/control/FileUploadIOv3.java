package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.N2oFileUpload;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента загрузки файлов версии 3.0
 */
@Component
public class FileUploadIOv3 extends BaseFileUploadIOv3<N2oFileUpload> {

    @Override
    public Class<N2oFileUpload> getElementClass() {
        return N2oFileUpload.class;
    }

    @Override
    public String getElementName() {
        return "file-upload";
    }
}
