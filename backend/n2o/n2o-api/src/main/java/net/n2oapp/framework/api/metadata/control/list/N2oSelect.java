package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент выбора из выпадающего списка
 */
@Getter
@Setter
@VisualComponent
public class N2oSelect extends N2oSingleListFieldAbstract {
    @VisualAttribute
    private ListType type;
    @VisualAttribute
    private Boolean cleanable;
    @VisualAttribute
    private String selectFormat;
    @VisualAttribute
    private String selectFormatOne;
    @VisualAttribute
    private String selectFormatFew;
    @VisualAttribute
    private String selectFormatMany;
    @VisualAttribute
    private String descriptionFieldId;

    @Override
    public boolean isSingle() {
        return !ListType.multi.equals(type) && !ListType.checkboxes.equals(type);
    }
}
