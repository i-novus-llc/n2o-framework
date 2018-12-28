package net.n2oapp.framework.config.reader.widget.widget3;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oTree;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.GroupingNodes;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.InheritanceNodes;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.api.metadata.reader.ElementReaderFactory;
import net.n2oapp.framework.config.reader.MetadataReaderException;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        n2oTree.setSearch(getElementBoolean(element, "search"));
        n2oTree.setExpand(getElementBoolean(element, "expand"));
        n2oTree.setCheckboxes(getElementBoolean(element, "checkboxes"));
        n2oTree.setAutoSelect(getElementBoolean(element, "auto-select"));
        Element in = element.getChild("inheritance-nodes", namespace);
        Element gn = element.getChild("grouping-nodes", namespace);
        if (in != null) n2oTree.setInheritanceNodes(new TypedElementReader<InheritanceNodes>() {
            @Override
            public String getElementName() {
                return "inheritance-nodes";
            }

            @Override
            public InheritanceNodes read(Element element) {
                InheritanceNodes inheritanceNodes = new InheritanceNodes();
                inheritanceNodes.setParentFieldId(getAttributeString(element, "parent-field-id"));
                inheritanceNodes.setLabelFieldId(getAttributeString(element, "label-field-id"));
                inheritanceNodes
                        .setHasChildrenFieldId(getAttributeString(element, "has-children-field-id"));
                inheritanceNodes.setSearchFieldId(getAttributeString(element, "search-field-id"));
                inheritanceNodes.setCanResolvedFieldId(getAttributeString(element, "can-resolved-field-id"));
                inheritanceNodes.setEnabledFieldId(getAttributeString(element,"enabled-field-id"));
                inheritanceNodes.setIconFieldId(getAttributeString(element, "icon-field-id"));
                return inheritanceNodes;
            }

            @Override
            public Class<InheritanceNodes> getElementClass() {
                return InheritanceNodes.class;
            }
        }.read(in));
        if (gn != null) n2oTree.setGroupingNodes(new TypedElementReader<GroupingNodes>() {
            @Override
            public String getElementName() {
                return "grouping-nodes";
            }

            @Override
            public GroupingNodes read(Element element) {
                GroupingNodes groupingNodesNodes = new GroupingNodes();
                groupingNodesNodes.setSearchFieldId(getAttributeString(element, "search-field-id"));
                List<Element> nodes = element.getChildren("node", namespace);
                groupingNodesNodes.setNodes(readNodes(nodes, namespace));
                return groupingNodesNodes;
            }

            @Override
            public Class<GroupingNodes> getElementClass() {
                return GroupingNodes.class;
            }
        }.read(gn));
        readWidgetDefinition(element, namespace, n2oTree);
    }

    private static List<GroupingNodes.Node> readNodes(List<Element> nodes, Namespace namespace) {
        List<GroupingNodes.Node> res = new ArrayList<>();
        for (Element el : nodes) {
            GroupingNodes.Node node = new GroupingNodes.Node();
            node.setValueFieldId(getAttributeString(el, "value-field-id"));
            node.setLabelFieldId(getAttributeString(el, "label-field-id"));
            node.setCanResolved(getAttributeBoolean(el, "can-resolved"));
            node.setEnabled(getAttributeBoolean(el, "enabled"));
            node.setIcon(getAttributeString(el, "icon"));
            node.setNodes(readNodes(el.getChildren("node", namespace), namespace));
            res.add(node);
        }
        return res;
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
