package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.ShowModalPageFromClassifier;

/**
 * Компонент выбора из выпадающего списка
 */
@Getter
@Setter
public class N2oSelect extends N2oSingleListFieldAbstract {
    private ShowModalPageFromClassifier showModal;
    private Boolean searchAsYouType;
    private Boolean wordWrap;
    private N2oClassifier.Mode mode;
    private ListType type;
    private Boolean cleanable;
    private String selectFormat;
    private String selectFormatOne;
    private String selectFormatFew;
    private String selectFormatMany;
    private String descriptionFieldId;

    @Override
    public boolean isSingle() {
        return !ListType.multi.equals(type) && !ListType.checkboxes.equals(type);
    }
}
