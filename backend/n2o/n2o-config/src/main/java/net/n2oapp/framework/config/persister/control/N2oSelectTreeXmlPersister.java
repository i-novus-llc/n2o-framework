package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.list.N2oSelectTree;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.GroupingNodes;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.InheritanceNodes;
import net.n2oapp.framework.config.persister.tools.PreFilterPersister;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

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