package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.control.list.N2oSingleListFieldAbstract;

/**
 * Компонент ввода текста с автоподбором
 */
@Getter
@Setter
@VisualComponent
public class N2oAutoComplete extends N2oSingleListFieldAbstract {
    @VisualAttribute
    private Boolean tags;
    @VisualAttribute
    private Integer maxTagTextLength;
}
