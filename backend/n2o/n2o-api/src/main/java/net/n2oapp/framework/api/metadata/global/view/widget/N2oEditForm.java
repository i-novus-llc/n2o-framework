package net.n2oapp.framework.api.metadata.global.view.widget;

import net.n2oapp.framework.api.ui.FormModel;

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
