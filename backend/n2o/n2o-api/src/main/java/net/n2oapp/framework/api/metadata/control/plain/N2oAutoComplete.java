package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.list.N2oSingleListFieldAbstract;

/**
 * Компонент ввода текста с автоподбором
 */
@Getter
@Setter
public class N2oAutoComplete extends N2oSingleListFieldAbstract {
    private Boolean tags;
    private Integer maxTagTextLength;
}
