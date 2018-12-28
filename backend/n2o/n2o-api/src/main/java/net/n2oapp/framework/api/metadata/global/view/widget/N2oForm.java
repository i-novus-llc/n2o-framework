package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

/**
* Исходная модель формы
 */
@Getter
@Setter
public class N2oForm extends N2oWidget {
    private String modalWidth;
    private String layout;
    private NamespaceUriAware items[];
    private String defaultValuesQueryId;
    private FormMode mode;

    @Override
    public boolean isEditable() {
        return true;
    }
}
