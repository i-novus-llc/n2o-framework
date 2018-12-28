package net.n2oapp.framework.api.metadata.global.view.widget;

import net.n2oapp.framework.api.ui.FormModel;

/**
 * User: iryabov
 * Date: 05.02.13
 * Time: 19:08
 */
@Deprecated
public class N2oEditForm extends N2oForm {

    private FormModel model;

    public FormModel getModel() {
        return model;
    }

    public void setModel(FormModel model) {
        this.model = model;
    }
}
