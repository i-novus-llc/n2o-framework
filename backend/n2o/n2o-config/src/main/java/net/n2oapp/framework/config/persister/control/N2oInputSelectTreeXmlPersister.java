package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.list.N2oInputSelectTree;
import net.n2oapp.framework.api.metadata.control.list.N2oSelectTree;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * @author iryabov
 * @since 06.12.2016
 */
public class N2oInputSelectTreeXmlPersister extends N2oControlXmlPersister<N2oInputSelectTree> {

    @Override
    public Element persist(N2oInputSelectTree field, Namespace namespace) {
        Element rootElement = new Element(getElementName(), namespacePrefix, namespaceUri);
        setControl(rootElement, field);
        setField(rootElement, field);
        setListField(rootElement, field);
        setTreeField(rootElement, field);
        return rootElement;
    }

    @Override
    public Class<N2oInputSelectTree> getElementClass() {
        return N2oInputSelectTree.class;
    }

    @Override
    public String getElementName() {
        return "input-select-tree";
    }
}
