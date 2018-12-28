package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.list.N2oClassifier;
import net.n2oapp.framework.api.metadata.control.list.N2oSelect;
import net.n2oapp.framework.config.reader.tools.showModal.ShowModalFromClassifierReaderV1;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeBoolean;

@Component
public class N2oClassifierXmlReaderV1 extends N2oStandardControlReaderV1<N2oSelect> {
    @Override
    public N2oSelect read(Element element, Namespace namespace) {
        return getClassifier(element, namespace);
    }

    @Override
    public Class<N2oSelect> getElementClass() {
        return N2oSelect.class;
    }

    @Override
    public String getElementName() {
        return "classifier";
    }

    public N2oSelect getClassifier(Element fieldSetElement, Namespace namespace) {
        N2oSelect n2oSelect = new N2oSelect();
        getListFieldDefinition(fieldSetElement, n2oSelect);
        Element showModalElement = fieldSetElement.getChild("show-modal", namespace);
        if (showModalElement != null)
            n2oSelect.setShowModal(ShowModalFromClassifierReaderV1.getInstance().read(showModalElement));
        n2oSelect.setSearchAsYouType(getAttributeBoolean(fieldSetElement, "search-as-you-type", "search-are-you-type"));
        n2oSelect.setWordWrap(getAttributeBoolean(fieldSetElement, "word-wrap"));
        boolean quick = n2oSelect.getQueryId() != null;
        boolean advance = n2oSelect.getShowModal() != null;
        N2oClassifier.Mode mode = quick && !advance ? N2oClassifier.Mode.quick
                : advance && !quick ? N2oClassifier.Mode.advance
                : N2oClassifier.Mode.combined;
        n2oSelect.setMode(mode);
        return n2oSelect;
    }


}
