package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.control.multi.N2oMultiListField;

/**
 * Компонент ввода input-select - выпадающий список
 */
@Getter
@Setter
public class N2oInputSelect extends N2oListField implements N2oSingleListField, N2oMultiListField {

    private ListType type;
    private Boolean storeOnInput;

    @Override
    public boolean isSingle() {
        return !ListType.multi.equals(type) && !ListType.checkboxes.equals(type);
    }
}
