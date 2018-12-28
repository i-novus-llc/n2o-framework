package net.n2oapp.framework.api.metadata.global.dao.invocation.model;

import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.GroupingNodes;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Свойства для получения древовидных выборок
 */
@Deprecated
public class N2oTreeInvocation implements N2oInvocation {
    private List<Node> root;
    private Map<String, Node> nodeMap;
    private String leafDomain;
    private N2oInvocation realExecution;
    private String namespaceUri;

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    public N2oTreeInvocation(CompiledQuery query, List<GroupingNodes.Node> list) {
        this.nodeMap = buildNodeMap(list, query);
        if (query.getLists() != null && query.getLists().length >0) {
            this.realExecution = query.getLists()[0].getInvocation();
        }
    }

    public List<Node> getRoot() {
        return root;
    }

    public String getLeafDomain() {
        return leafDomain;
    }

    public N2oInvocation getRealExecution() {
        return realExecution;
    }

    public Map<String, Node> getNodeMap() {
        return nodeMap;
    }


    private Map<String, Node> buildNodeMap(List<GroupingNodes.Node> list, CompiledQuery query) {
        Map<String, Node> res = new HashMap<>();
        this.root = new ArrayList<>();
        for (GroupingNodes.Node node : list) {
            Node n = buildNode(res, node, query);
            n.setParentId(null);
            res.put(n.getId(), n);
            root.add(n);
        }
        return res;
    }

    private Node buildNode(Map<String, Node> res, GroupingNodes.Node node, CompiledQuery query) {
        Node n = new Node();
        n.setId(node.getValueFieldId());
        N2oQuery.Field field = query.getFieldsMap().get(n.getId());
        if (field != null) n.setDomain(field.getDomain());
        n.setHasChildren(node.getNodes() != null && node.getNodes().size() != 0);
        n.setName(node.getLabelFieldId());
        n.getAdditionalProperties().put(Node.ICON_PROPERTY, node.getIcon());
        n.setEnabled(castDefault(node.getEnabled(), true));
        if (n.isHasChildren()) iterateList(node.getNodes(), res, n, query);
        else if (leafDomain == null) leafDomain = n.domain;
        return n;
    }

    private void iterateList(List<GroupingNodes.Node> list, Map<String, Node> res, Node parent, CompiledQuery query) {
        for (GroupingNodes.Node node : list) {
            Node n = buildNode(res, node, query);
            n.setParentId(parent.getId());
            res.put(n.getId(), n);
        }
    }


    public static class Node implements Serializable {
        private String id;
        private String parentId;
        private String name;
        private boolean hasChildren;
        private String domain;
        private boolean enabled;
        private Map<String, String> additionalProperties = new HashMap<>();

        public static final String ICON_PROPERTY = "icon";

        public String getDomain() {
            return domain;
        }

        private void setDomain(String domain) {
            this.domain = domain;
        }

        public String getId() {
            return id;
        }

        private void setId(String id) {
            this.id = id;
        }

        public String getParentId() {
            return parentId;
        }

        private void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getName() {
            return name;
        }

        private void setName(String name) {
            this.name = name;
        }

        public boolean isHasChildren() {
            return hasChildren;
        }

        private void setHasChildren(boolean hasChildren) {
            this.hasChildren = hasChildren;
        }

        public Map<String, String> getAdditionalProperties() {
            return additionalProperties;
        }

        public void setAdditionalProperties(Map<String, String> additionalProperties) {
            this.additionalProperties = additionalProperties;
        }

        @Deprecated
        public boolean getCanResolved() {
            return enabled;
        }

        public boolean getEnabled() {
            return enabled;
        }

        @Deprecated
        public boolean setCanResolved(boolean canResolved) {
            return this.enabled = canResolved;
        }

        public boolean setEnabled(boolean enabled) {
            return this.enabled = enabled;
        }
    }
}