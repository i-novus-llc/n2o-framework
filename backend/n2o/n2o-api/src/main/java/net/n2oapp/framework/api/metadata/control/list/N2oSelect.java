package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.ShowModalPageFromClassifier;

/**
 * Компонент ввода select выпадающий список
 */
@Getter
@Setter
public class N2oSelect extends N2oSingleListFieldAbstract {
    private ShowModalPageFromClassifier showModal;
    private Boolean searchAsYouType;
    private Boolean wordWrap;
    private N2oClassifier.Mode mode;
    private ListType type;
}
