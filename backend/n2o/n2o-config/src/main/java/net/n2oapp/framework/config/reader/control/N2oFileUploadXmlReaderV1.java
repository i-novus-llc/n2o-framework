package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.N2oFileUpload;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Created by schirkova on 22.09.2014.
 */
@Component
public class N2oFileUploadXmlReaderV1 extends N2oStandardControlReaderV1<N2oFileUpload> {
    @Override
    public String getElementName() {
        return "file-upload";
    }

    @Override
    public N2oFileUpload read(Element element, Namespace namespace) {
        N2oFileUpload fileUpload = new N2oFileUpload();
        getControlFieldDefinition(element, fileUpload);
        fileUpload.setMulti(getMulti(ReaderJdomUtil.getAttributeString(element, "mode")));
        fileUpload.setUploadUrl(ReaderJdomUtil.getAttributeString(element, "upload-url"));
        return fileUpload;
    }

    private Boolean getMulti(String mode) {
        if (mode == null) return null;
        return "multi".equals(mode);
    }

    @Override
    public Class<N2oFileUpload> getElementClass() {
        return N2oFileUpload.class;
    }
}
