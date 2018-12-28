package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.list.N2oInputSelectTree;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author iryabov
 * @since 06.12.2016
 */
@Component
public class N2oInputSelectTreeXmlReaderV1 extends N2oStandardControlReaderV1<N2oInputSelectTree> {

    @Override
    public String getElementName() {
        return "input-select-tree";
    }

    @Override
    public N2oInputSelectTree read(Element element, Namespace namespace) {
        N2oInputSelectTree field = new N2oInputSelectTree();
        getControlFieldDefinition(element, field);
        getTreeDefinition(element, namespace, field);
        return field;
    }

    @Override
    public Class<N2oInputSelectTree> getElementClass() {
        return N2oInputSelectTree.class;
    }
}
