package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.list.N2oSelectTree;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * User: dfirstov
 * Date: 10.02.15
 * Time: 10:22
 */
@Component
public class N2oSelectTreeXmlPersister extends N2oControlXmlPersister<N2oSelectTree> {

    @Override
    public Element persist(N2oSelectTree selectTree, Namespace namespace) {
        Element rootElement = new Element(getElementName(), namespacePrefix, namespaceUri);
        setControl(rootElement, selectTree);
        setField(rootElement, selectTree);
        setListField(rootElement, selectTree);
        setTreeField(rootElement, selectTree);
        return rootElement;
    }

    @Override
    public Class<N2oSelectTree> getElementClass() {
        return N2oSelectTree.class;
    }

    @Override
    public String getElementName() {
        return "select-tree";
    }
}