package net.n2oapp.framework.config.persister.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.tree.GroupingNodes;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.InheritanceNodes;
import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oTree;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;
import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setElementBoolean;

/**
 * User: operhod
 * Date: 10.01.14
 * Time: 10:22
 */

@Component
public class TreeXmlPersister extends WidgetXmlPersister<N2oTree> {
    @Override
    public Element getWidget(N2oTree n2o, Namespace namespace) {
        Element rootElement = new Element(getElementName(), namespace);
        persistWidget(rootElement, n2o, namespace);
        if (n2o.getAjax() != null) {
            Element ajax = new Element("ajax", namespace);
            ajax.addContent(n2o.getAjax().toString());
            rootElement.addContent(ajax);
            setElementBoolean(rootElement, "search", n2o.getSearch());
            setElementBoolean(rootElement, "expand", n2o.getExpand());
        }
        if (n2o.getCheckboxes() != null) {
            Element checkboxes = new Element("checkboxes", namespace);
            checkboxes.addContent(n2o.getCheckboxes().toString());
            rootElement.addContent(checkboxes);
        }
        if (n2o.getInheritanceNodes() != null) {
            InheritanceNodes in = n2o.getInheritanceNodes();
            Element inheritanceNodes = new Element("inheritance-nodes", namespace);
            inheritanceNodes.setAttribute("parent-field-id", n2o.getInheritanceNodes().getParentFieldId());
            inheritanceNodes.setAttribute("label-field-id", n2o.getInheritanceNodes().getLabelFieldId());
            if (in.getHasChildrenFieldId() != null)
                inheritanceNodes.setAttribute("has-children-field-id", in.getHasChildrenFieldId());
            if (in.getSearchFieldId() != null)
                inheritanceNodes.setAttribute("search-field-id", in.getSearchFieldId());
            if (in.getEnabledFieldId() != null)
                inheritanceNodes.setAttribute("enabled-field-id", in.getEnabledFieldId());
            if (in.getIconFieldId() != null)
                inheritanceNodes.setAttribute("icon-field-id", in.getIconFieldId());
            rootElement.addContent(inheritanceNodes);
        } else if (n2o.getGroupingNodes() != null) {
            List<GroupingNodes.Node> nodes = n2o.getGroupingNodes().getNodes();
            Element groupingNodes = new Element("grouping-nodes", namespace);
            if (n2o.getGroupingNodes().getSearchFieldId() != null)
                groupingNodes.setAttribute("search-field-id", n2o.getGroupingNodes().getSearchFieldId());
            rootElement.addContent(groupingNodes);
            createNode(nodes.get(0), namespace, groupingNodes);
        }
        setElementBoolean(rootElement, "auto-select", n2o.getAutoSelect());
        return rootElement;
    }

    private void createNode(GroupingNodes.Node node, Namespace namespace, Element root) {
        Element nodeElement = new Element("node", namespace);
        nodeElement.setAttribute("value-field-id", node.getValueFieldId());
        nodeElement.setAttribute("label-field-id", node.getLabelFieldId());
        if (node.getEnabled() != null)
            setAttribute(nodeElement, "enabled", node.getEnabled());
        if (node.getIcon() != null)
            setAttribute(nodeElement, "icon", node.getIcon());
        root.addContent(nodeElement);
        if (node.getNodes() != null && node.getNodes().size() > 0)
            createNode(node.getNodes().get(0), namespace, nodeElement);
    }

    @Override
    public Class<N2oTree> getElementClass() {
        return N2oTree.class;
    }

    @Override
    public String getElementName() {
        return "tree";
    }
}
