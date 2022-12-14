package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.N2oComponent;

/**
 * Компонент выбора из выпадающего списка
 */
@Getter
@Setter
@N2oComponent
public class N2oSelect extends N2oSingleListFieldAbstract {
    @N2oAttribute
    private ListType type;
    @N2oAttribute
    private Boolean cleanable;
    @N2oAttribute
    private String selectFormat;
    @N2oAttribute
    private String selectFormatOne;
    @N2oAttribute
    private String selectFormatFew;
    @N2oAttribute
    private String selectFormatMany;
    @N2oAttribute
    private String descriptionFieldId;

    @Override
    public boolean isSingle() {
        return !ListType.multi.equals(type) && !ListType.checkboxes.equals(type);
    }
}
