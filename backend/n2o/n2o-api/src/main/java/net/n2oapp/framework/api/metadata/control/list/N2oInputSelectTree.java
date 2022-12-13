package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент ввода с выбором в выпадающем списке в виде дерева
 */
@Getter
@Setter
@VisualComponent
public class N2oInputSelectTree extends N2oSelectTree {
    @VisualAttribute
    private Integer maxTagTextLength;
}
