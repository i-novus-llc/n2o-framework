package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.control.N2oListField;

/**
 * Компонент ввода текста с выбором из выпадающего списка
 */
@Getter
@Setter
@VisualComponent
public class N2oInputSelect extends N2oListField {
    @VisualAttribute
    private ListType type;
    @VisualAttribute
    private Boolean resetOnBlur;
    @VisualAttribute
    private String descriptionFieldId;
    @VisualAttribute
    private Integer maxTagTextLength;

    @Override
    public boolean isSingle() {
        return !ListType.multi.equals(type) && !ListType.checkboxes.equals(type);
    }
}
