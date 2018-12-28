package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.list.ListType;
import net.n2oapp.framework.api.metadata.control.list.N2oClassifier;
import net.n2oapp.framework.api.metadata.control.list.N2oSelect;
import net.n2oapp.framework.config.reader.tools.showModal.ShowModalFromClassifierReaderV1;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeBoolean;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeEnum;

@Component
public class N2oSelectXmlReaderV1 extends N2oStandardControlReaderV1<N2oSelect> {
    @Override
    public N2oSelect read(Element element, Namespace namespace) {
        N2oSelect select = new N2oSelect();
        select.setType(getAttributeEnum(element, "type", ListType.class));
        Element showModalElement = element.getChild("show-modal", namespace);
        if (showModalElement != null)
            select.setShowModal(ShowModalFromClassifierReaderV1.getInstance().read(showModalElement));
        select.setSearchAsYouType(getAttributeBoolean(element, "search-as-you-type", "search-are-you-type"));
        select.setSearch(getAttributeBoolean(element, "search"));
        select.setWordWrap(getAttributeBoolean(element, "word-wrap"));
        boolean quick = select.getQueryId() != null;
        boolean advance = select.getShowModal() != null;
        N2oClassifier.Mode mode = quick && !advance ? N2oClassifier.Mode.quick
                : advance && !quick ? N2oClassifier.Mode.advance
                : N2oClassifier.Mode.combined;
        select.setMode(mode);
        return (N2oSelect) getQueryFieldDefinition(element, select);
    }

    @Override
    public Class<N2oSelect> getElementClass() {
        return N2oSelect.class;
    }

    @Override
    public String getElementName() {
        return "select";
    }
}
