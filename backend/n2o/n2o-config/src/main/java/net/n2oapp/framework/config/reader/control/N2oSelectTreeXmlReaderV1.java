package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.list.N2oSelectTree;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Created by dfirstov on 10.02.2015.
 */
@Component
public class N2oSelectTreeXmlReaderV1 extends N2oStandardControlReaderV1<N2oSelectTree> {

    @Override
    public String getElementName() {
        return "select-tree";
    }

    @Override
    public N2oSelectTree read(Element element, Namespace namespace) {
        N2oSelectTree selectTree = new N2oSelectTree();
        getControlFieldDefinition(element, selectTree);
        getTreeDefinition(element, namespace, selectTree);
        return selectTree;
    }

    @Override
    public Class<N2oSelectTree> getElementClass() {
        return N2oSelectTree.class;
    }
}
