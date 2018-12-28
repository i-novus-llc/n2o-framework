package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.N2oFileUpload;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Created by enuzhdina on 16.06.2015.
 */
@Component
public class N2oFileUploadPersister extends N2oControlXmlPersister<N2oFileUpload> {
    @Override
    public Element persist(N2oFileUpload control, Namespace namespace) {
        Element rootElement = new Element(getElementName(), namespacePrefix, namespaceUri);
        setControl(rootElement, control);
        setField(rootElement, control);
        PersisterJdomUtil.setAttribute(rootElement, "mode", getMode(control.getMulti()));
        PersisterJdomUtil.setAttribute(rootElement, "upload-url", control.getUploadUrl());
        return rootElement;
    }

    private String getMode(Boolean multi) {
        if (multi == null) return null;
        return multi ? "multi" : "single";
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
