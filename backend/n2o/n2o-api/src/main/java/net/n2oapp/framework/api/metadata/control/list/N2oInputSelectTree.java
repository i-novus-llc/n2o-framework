package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ввода с выбором в выпадающем списке в виде дерева
 */
@Getter
@Setter
public class N2oInputSelectTree extends N2oSelectTree {
    private Integer maxTagTextLength;
    private Integer throttleDelay;
    private Integer searchMinLength;
}
