package net.n2oapp.framework.api.metadata.control.list;

/**
 * @author iryabov
 * @since 06.12.2016
 * Компонент ввода с выбором в выпадающем списке в виде дерева
 */
public class N2oInputSelectTree extends N2oSelectTree {

    @Override
    public String getLabelFieldId() {
        return getInheritanceNodes() != null ? getInheritanceNodes().getLabelFieldId() : null;
    }
}
