package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;

/**
 * Исходная модель виджета MultiForm
 */
@Getter
@Setter
public class N2oMultiForm extends N2oAbstractListWidget  {
    private Boolean unsavedDataPrompt;
    private N2oForm form;

    @Override
    public ReduxModelEnum getModel() {
        return ReduxModelEnum.DATASOURCE;
    }
}
