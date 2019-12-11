package net.n2oapp.framework.config.reader.widget.widget3;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oTree;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.*;

@Component
public class TreeXmlReaderV3 extends WidgetBaseXmlReaderV3<N2oWidget> {
    @Override
    public N2oWidget read(Element element, Namespace namespace) {
        N2oTree n2oTree = new N2oTree();
        readRef(element, n2oTree);
        getAbstractTreeDefinition(element, namespace, n2oTree, readerFactory);
        return n2oTree;
    }

    private void getAbstractTreeDefinition(Element element, Namespace namespace, N2oTree n2oTree,
                                           NamespaceReaderFactory extensionReaderFactory) {
        n2oTree.setAjax(getElementBoolean(element, "ajax"));
        n2oTree.setCheckboxes(getElementBoolean(element, "checkboxes"));
        Element in = element.getChild("inheritance-nodes", namespace);
        if (in != null) {
            n2oTree.setParentFieldId(getAttributeString(in, "parent-field-id"));
            n2oTree.setLabelFieldId(getAttributeString(in, "label-field-id"));
            n2oTree.setHasChildrenFieldId(getAttributeString(in, "has-children-field-id"));
            n2oTree.setIconFieldId(getAttributeString(in, "icon-field-id"));
        }
        readWidgetDefinition(element, namespace, n2oTree);
    }

    @Override
    public Class<N2oWidget> getElementClass() {
        return N2oWidget.class;
    }

    @Override
    public String getElementName() {
        return "tree";
    }
}
