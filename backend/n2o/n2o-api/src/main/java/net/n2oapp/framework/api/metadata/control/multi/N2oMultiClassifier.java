package net.n2oapp.framework.api.metadata.control.multi;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ввода: выпадающий список с множественным выбором
 */
@Getter
@Setter
public class N2oMultiClassifier extends N2oMultiListFieldAbstract {

    private Boolean searchAsYouType;

    public N2oMultiClassifier() {
    }

    public N2oMultiClassifier(String id) {
        setId(id);
    }
}
