package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oListField;

/**
 * Компонент ввода текста с выбором из выпадающего списка
 */
@Getter
@Setter
public class N2oInputSelect extends N2oListField {
    private ListTypeEnum type;
    private Boolean resetOnBlur;
    private String descriptionFieldId;
    private Integer maxTagCount;
    private Integer maxTagTextLength;
    private Integer throttleDelay;
    private Integer searchMinLength;

    @Override
    public boolean isSingle() {
        return !ListTypeEnum.MULTI.equals(type) && !ListTypeEnum.CHECKBOXES.equals(type);
    }
}
