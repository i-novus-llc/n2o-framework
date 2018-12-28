package net.n2oapp.framework.standard.header.context;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.user.StaticUserContext;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.standard.header.model.global.context.UserContextStructure;
import net.n2oapp.framework.standard.header.model.local.CompiledStandardHeader;

import java.util.*;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;

/**
 * User: operhod
 * Date: 20.05.14
 * Time: 10:12
 */
public class UserContextModelBuilder {


    /**
     * @param contextStructure - структура контекста
     * @param contextData      - исходная информация о контексте
     */
    public CompiledStandardHeader.UserContext build(UserContextStructure contextStructure, Collection<DataSet> contextData) {
        UserContext user = StaticUserContext.getUserContext();
        checkSelectedContext(contextStructure, contextData, user);
        TreeStructure treeStructure = buildTmpTreeStructure(contextStructure, contextData, user);
        return new CompiledStandardHeader.UserContext(
                buildFlatStructure(contextStructure, contextData, treeStructure),
                buildContextLabel(contextStructure.getPosition(), contextData, user));
    }

    private List<CompiledStandardHeader.ContextLabelNode> buildContextLabel(UserContextStructure.Position position, Collection<DataSet> contextData, UserContext user) {
        LinkedList<CompiledStandardHeader.ContextLabelNode> label = new LinkedList<>();
        Object positionContextValue = getContextValue(user, position.getValueFieldId());
        if (positionContextValue == null)
            return singletonList(new CompiledStandardHeader.ContextLabelNode("n2o.context"));
        findLabelUnit(position.getUnits(), label, user);
        Object contextValue = getContextValue(user, position.getLabelFieldId());
        label.add(new CompiledStandardHeader.ContextLabelNode(contextValue != null ? (String) contextValue : ""));
        return label;
    }

    private void findLabelUnit(UserContextStructure.Unit[] units, LinkedList<CompiledStandardHeader.ContextLabelNode> label, UserContext user) {
        if (units != null)
            for (UserContextStructure.Unit unit : units) {
                Object value = getContextValue(user, unit.getValueFieldId());
                if (value != null) {
                    Object contextValue = getContextValue(user, unit.getLabelFieldId());
                    if (contextValue != null) {
                        label.add(new CompiledStandardHeader.ContextLabelNode(contextValue.toString()));
                        findLabelUnit(unit.getUnits(), label, user);
                    }
                    break;
                }

            }
    }


    private TreeStructure buildTmpTreeStructure(UserContextStructure contextStructure, Collection<DataSet> contextData, UserContext user) {
        TreeStructure res = new TreeStructure();
        iterateUnits(contextStructure.getPosition(), unit -> res.addActionParam(unit.getValueFieldId()));
        res.nodes = (unitToSet(contextStructure.getPosition(), contextData, null, res, user));
        return res;
    }

    public static void iterateUnits(UserContextStructure.Unit unit, Consumer<UserContextStructure.Unit> unitConsumer) {
        if (unit != null) {
            unitConsumer.accept(unit);
            if (unit.getUnits() != null)
                for (UserContextStructure.Unit subUnit : unit.getUnits()) {
                    iterateUnits(subUnit, unitConsumer);
                }
        }
    }

    private void checkSelectedContext(UserContextStructure contextStructure, Collection<DataSet> contextData, UserContext user) {
        Object positionValue = getContextValue(user, contextStructure.getPosition().getValueFieldId());
        if (positionValue == null && contextData.size() > 0) {
            Map<String, Object> context = getFirstContext(collectParams(contextStructure), contextData);
            setContextValues(user, context);
        }
    }

    private static Map<String, Object> getFirstContext(Set<String> strings, Collection<DataSet> contextData) {
        Map<String, Object> res = new HashMap<>();
        DataSet dataSet = contextData.iterator().next();
        for (String key : strings) {
            res.put(key, dataSet.get(key));
        }
        return res;
    }


    private static Set<String> collectParams(UserContextStructure structure) {
        Set<String> res = new HashSet<>();
        res.add(structure.getPosition().getValueFieldId());
        addUnitsContextParams(res, structure.getUnits());
        return res;
    }

    private static void addUnitsContextParams(Set<String> res, UserContextStructure.Unit[] units) {
        if (units != null && units.length > 0) {
            res.add(units[0].getValueFieldId());
            addUnitsContextParams(res, units[0].getUnits());
        }
    }

    private Set<Node> unitToSet(UserContextStructure.Unit root, Collection<DataSet> contextData, Node parentNode, TreeStructure res, UserContext user) {
        Set<Node> nodes = new HashSet<>();
        for (DataSet dataSet : contextData) {
            Object value = dataSet.get(root.getValueFieldId());
            if (value == null || nodes.contains(new Node(value))) continue;
            Node node = new Node();
            node.id = value;
            node.parentNode = parentNode;
            Object labelField = dataSet.get(root.getLabelFieldId());
            if (labelField != null)
                node.name = labelField.toString();
            node.nodes = new HashSet<>();
            node.fieldId = root.getValueFieldId();
            if (root instanceof UserContextStructure.Position) {
                node.hintFieldId = ((UserContextStructure.Position) root).getHintFieldId();
                node.isPosition = true;
            }
            node.contextParam = root.getValueFieldId();
            node.isCurrentContextParam = (parentNode == null || parentNode.isCurrentContextParam)
                    && value.equals(getContextValue(user, root.getValueFieldId()));
            if (root.getUnits() != null) for (UserContextStructure.Unit unit : root.getUnits()) {
                Set<Node> set = unitToSet(unit, filter(contextData, root.getValueFieldId(), node.id), node, res, user);
                node.nodes.addAll(set);
            }


            //фэйковая орг. структура
            if (node.nodes.size() == 0 && node.isPosition) {
                node.nodes.add(createFakeNode(res, node));
            }


            node.isOn = !containsCurrentContextParams(node.nodes) && node.isCurrentContextParam;
            if (node.isOn) res.chosenNode = node;
            nodes.add(node);
        }
        return nodes;
    }

    private static Node createFakeNode(TreeStructure res, Node node) {
        //фэйковый нод, создается в случае когда у position нету вложенной орг. структуры.
        //нужен для того, чтобы было во что тыкнуть в header
        assert node.isPosition;
        assert node.nodes.size() == 0;

        Node fakeNode = new Node();
        fakeNode.id = node.id;
        fakeNode.parentNode = node;
        fakeNode.name = node.name;
        fakeNode.fieldId = node.fieldId;
        fakeNode.hintFieldId = node.hintFieldId;
        fakeNode.contextParam = node.contextParam;
        fakeNode.isCurrentContextParam = node.isCurrentContextParam;
        fakeNode.isOn = node.isCurrentContextParam;
        if (fakeNode.isOn) res.chosenNode = node;
        return fakeNode;
    }

    private static boolean containsCurrentContextParams(Set<Node> set) {
        for (Node node : set) {
            if (node.isCurrentContextParam) return true;
        }
        return false;
    }


    private static List<CompiledStandardHeader.ContextLabelNode> buildContextLabel(UserContextStructure contextStructure, Collection<DataSet> contextData, Node chosenNode) {
        LinkedList<CompiledStandardHeader.ContextLabelNode> label = new LinkedList<>();
        if (chosenNode == null) return Arrays.asList(new CompiledStandardHeader.ContextLabelNode("n2o.context"));
        addLabelNode(chosenNode, label);
        return label;
    }

    private static void addLabelNode(Node node, LinkedList<CompiledStandardHeader.ContextLabelNode> label) {
        CompiledStandardHeader.ContextLabelNode labelNode = new CompiledStandardHeader.ContextLabelNode(node.name);
        if (node.isPosition)
            label.add(labelNode);
        else
            label.addFirst(labelNode);
        if (node.parentNode != null) addLabelNode(node.parentNode, label);
    }


    protected List<CompiledStandardHeader.RootContextNode> buildFlatStructure(UserContextStructure contextStructure, Collection<DataSet> contextData, TreeStructure treeStructure) {
        List<CompiledStandardHeader.RootContextNode> list = new ArrayList<>();
        int level = 0;
        Increment inc = new Increment();
        for (Node node : treeStructure.nodes) {
            CompiledStandardHeader.RootContextNode rootContextNode = new CompiledStandardHeader.RootContextNode();
            rootContextNode.setId(inc.next());
            rootContextNode.setUnitName(node.name);
            DataSet next = filter(contextData, contextStructure.getPosition().getValueFieldId(), node.id).iterator().next();
            if (contextStructure.getPosition().getHintFieldId() != null) {
                Object hintField = next.get(contextStructure.getPosition().getHintFieldId());
                if (hintField != null)
                    rootContextNode.setHint(hintField.toString());
            }
            Map<String, Object> actions = new HashMap<>(treeStructure.actionParams);
            actions.put(node.contextParam, node.id);
            rootContextNode.setNodes(collectNodes(node.nodes, new ArrayList<CompiledStandardHeader.ContextNode>(), level, inc, actions));
            list.add(rootContextNode);
        }
        return list;
    }


    private static List<CompiledStandardHeader.ContextNode> collectNodes(Set<Node> nodes, ArrayList<CompiledStandardHeader.ContextNode> contextNodes, int level, Increment inc, Map<String, Object> actions) {
        level++;
        if (nodes != null)
            for (Node node : nodes) {
                CompiledStandardHeader.ContextNode contextNode = new CompiledStandardHeader.ContextNode();
                contextNode.setUnitName(node.name);
                contextNode.setLevel(level);
                contextNode.setId(inc.next());
                contextNode.setOn(node.isOn);
                actions = new HashMap<>(actions);
                actions.put(node.contextParam, node.id);
                contextNode.setActionParameters(actions);
                contextNodes.add(contextNode);
                collectNodes(node.nodes, contextNodes, level, inc, actions);
            }
        return contextNodes;
    }


    private static Collection<DataSet> filter(Collection<DataSet> contextData, String key, Object value) {
        Collection<DataSet> res = new ArrayList<>();
        for (DataSet dataSet : contextData) {
            if (dataSet == null)
                throw new RuntimeException("available context dataset is unexpectedly null");
            if (value == null)
                throw new RuntimeException(String.format("[%s] is null in current context", key));
            if (value.equals(dataSet.get(key))) res.add(dataSet);
        }
        return res;
    }

    protected Object getContextValue(UserContext user, String name) {
        return user.get(name);
    }

    protected void setContextValues(UserContext user, Map<String, Object> values) {
        user.set(values);
    }

    private static class TreeStructure {
        private Set<Node> nodes;
        private Node chosenNode;
        private Map<String, Object> actionParams = new HashMap<>();

        private void addActionParam(String param) {
            actionParams.put(param, null);
        }
    }

    private static class Node {
        private Set<Node> nodes;
        private Node parentNode;
        private String fieldId;
        private String hintFieldId;
        private String contextParam;
        private boolean isPosition;
        private boolean isCurrentContextParam;
        protected Object id;
        private String name;
        private boolean isOn;

        private Node() {
        }

        private Node(Object id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            if (id != null ? !id.equals(node.id) : node.id != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }

    private static class Increment {
        private int value = 0;

        public int next() {
            return value++;
        }
    }
}
