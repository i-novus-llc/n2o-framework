package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.N2oFileUpload;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента загрузки файлов версии 3.0
 */
@Component
public class FileUploadIOv3 extends BaseFileUploadIOv3<N2oFileUpload> {

    @Override
    public void io(Element e, N2oFileUpload m, IOProcessor p) {
        super.io(e, m, p);
    }

    @Override
    public Class<N2oFileUpload> getElementClass() {
        return N2oFileUpload.class;
    }

    @Override
    public String getElementName() {
        return "file-upload";
    }
}
