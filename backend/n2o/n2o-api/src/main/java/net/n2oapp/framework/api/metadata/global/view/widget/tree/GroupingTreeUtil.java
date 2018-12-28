package net.n2oapp.framework.api.metadata.global.view.widget.tree;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oTreeInvocation;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;

import java.util.*;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * @author dfirstov
 * @since 10.02.2015
 */
public class GroupingTreeUtil {
    public static final String TREE_PARENT_FIELD_ID = "tree.parent.id";
    public static final String TREE_PARENT_IS_NULL_FILTER_FIELD = "tree.parentIsNull";
    public static final String TREE_HAS_CHILDREN_FIELD_ID = "tree.hasChildren";
    public static final String TREE_LABEL_FIELD_ID = "tree.name";
    public static final String TREE_ICON_FIELD_ID = "tree.icon";
    @Deprecated //@see TREE_IS_ENABLED_FIELD_ID
    public static final String TREE_CAN_RESOLVED_FIELD_ID = "tree.canResolved";
    public static final String TREE_IS_ENABLED_FIELD_ID = "tree.enabled";
    public static final String TREE_FIELD_ID = N2oQuery.Field.PK;
    public static final Set<String> TREE_FIELDS = new HashSet<>();

    static {
        TREE_FIELDS.add(TREE_FIELD_ID);
        TREE_FIELDS.add(TREE_HAS_CHILDREN_FIELD_ID);
        TREE_FIELDS.add(TREE_LABEL_FIELD_ID);
        TREE_FIELDS.add(TREE_PARENT_FIELD_ID);
        TREE_FIELDS.add(TREE_PARENT_IS_NULL_FILTER_FIELD);
        TREE_FIELDS.add(TREE_ICON_FIELD_ID);
        TREE_FIELDS.add(TREE_CAN_RESOLVED_FIELD_ID);
        TREE_FIELDS.add(TREE_IS_ENABLED_FIELD_ID);
    }
    public static final String HIDE_ID = "hideId";



    public static N2oQuery initSourceQuery(CompiledQuery compiledQuery, GroupingNodes groupingNodes) {
        N2oTreeInvocation execution = new N2oTreeInvocation(compiledQuery, groupingNodes.getNodes());
        N2oQuery query = new N2oQuery();
        N2oQuery.Selection inv = new N2oQuery.Selection(N2oQuery.Selection.Type.list);
        inv.setInvocation(execution);
        query.setLists(new N2oQuery.Selection[]{inv});
        query.setName("");
        query.setObjectId(compiledQuery.getObject() != null ? compiledQuery.getObject().getId() : null);
        query.setFields(createFields(new LinkedHashSet<>(compiledQuery.getFieldsMap().values())));
        return query;
    }

    public static N2oQuery.Field[] createFields(Set<N2oQuery.Field> fields) {
        hideIdField(fields);
        N2oQuery.Field id = new N2oQuery.Field();
        id.setId(TREE_FIELD_ID);
        id.setExpression((TREE_FIELD_ID));
        id.setDomain("string");
        fields.add(id);

        N2oQuery.Field parentId = new N2oQuery.Field();
        parentId.setId(TREE_PARENT_FIELD_ID);
        parentId.setExpression((TREE_PARENT_FIELD_ID));
        parentId.setNoSorting(true);
        parentId.setDomain("string");
        parentId.setFilterList(new N2oQuery.Filter[]{
                new N2oQuery.Filter(TREE_PARENT_FIELD_ID, FilterType.eq),
                new N2oQuery.Filter(TREE_PARENT_IS_NULL_FILTER_FIELD, FilterType.isNull)});
        fields.add(parentId);

        N2oQuery.Field labelId = new N2oQuery.Field();
        labelId.setId(TREE_LABEL_FIELD_ID);
        labelId.setExpression((TREE_LABEL_FIELD_ID));
        labelId.setNoSorting(true);
        labelId.setDomain("string");
        fields.add(labelId);

        N2oQuery.Field hasChildren = new N2oQuery.Field();
        hasChildren.setId(TREE_HAS_CHILDREN_FIELD_ID);
        hasChildren.setExpression((TREE_HAS_CHILDREN_FIELD_ID));
        hasChildren.setNoSorting(true);
        hasChildren.setDomain("string");
        fields.add(hasChildren);

        N2oQuery.Field iconField = new N2oQuery.Field();
        iconField.setId(TREE_ICON_FIELD_ID);
        iconField.setExpression(TREE_ICON_FIELD_ID);
        iconField.setNoSorting(true);
        iconField.setDomain("string");
        fields.add(iconField);

        N2oQuery.Field canResolved = new N2oQuery.Field();
        canResolved.setId(TREE_CAN_RESOLVED_FIELD_ID);
        canResolved.setExpression(TREE_CAN_RESOLVED_FIELD_ID);
        canResolved.setNoSorting(true);
        canResolved.setDomain("boolean");
        fields.add(canResolved);

        N2oQuery.Field enabled = new N2oQuery.Field();
        enabled.setId(TREE_IS_ENABLED_FIELD_ID);
        enabled.setExpression(TREE_IS_ENABLED_FIELD_ID);
        enabled.setNoSorting(true);
        enabled.setDomain("boolean");
        fields.add(enabled);

        return fields.toArray(new N2oQuery.Field[fields.size()]);
    }

    public static void compileGrouppingNodes(List<GroupingNodes.Node> nodes) {
        for (GroupingNodes.Node node : nodes) {
            node.setEnabled(castDefault(node.getEnabled(), true));
        }
    }

    public static void setSearchLastLabel(GroupingNodes groupingNodes, List<GroupingNodes.Node> nodes) {
        for (GroupingNodes.Node node : nodes) {
            if (node.getNodes().size() > 0)
                setSearchLastLabel(groupingNodes, node.getNodes());
            else
                groupingNodes.setSearchFieldId(node.getLabelFieldId());
        }
    }

    private static void hideIdField(Set<N2oQuery.Field> fields) {
        for (N2oQuery.Field field : new ArrayList<>(fields)) {
            if ("id".equals(field.getId())) {
                N2oQuery.Field hideId = new N2oQuery.Field();
                hideId.setId(HIDE_ID);
                hideId.setSelectBody(field.getSelectBody());
                hideId.setExpression(field.getExpression());
                hideId.setDomain(field.getDomain());
                fields.remove(field);
                fields.add(hideId);

            }
        }
    }
}
