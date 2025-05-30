package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент выбора из выпадающего списка
 */
@Getter
@Setter
public class N2oSelect extends N2oSingleListFieldAbstract {
    private ListTypeEnum type;
    private Boolean cleanable;
    private String selectFormat;
    private String selectFormatOne;
    private String selectFormatFew;
    private String selectFormatMany;
    private String descriptionFieldId;

    @Override
    public boolean isSingle() {
        return !ListTypeEnum.MULTI.equals(type) && !ListTypeEnum.CHECKBOXES.equals(type);
    }
}
