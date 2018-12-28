package net.n2oapp.framework.api.metadata.control;

import net.n2oapp.framework.api.metadata.event.action.N2oShowModal;

/**
 * User: iryabov
 * Date: 27.06.13
 * Time: 18:10
 */
@Deprecated
public class ShowModalPageFromClassifier extends N2oShowModal {

    private String labelFieldId;
    private String valueFieldId;
    private boolean compiled;


    public String getLabelFieldId() {
        return labelFieldId;
    }

    public void setLabelFieldId(String labelFieldId) {
        this.labelFieldId = labelFieldId;
    }

    public String getValueFieldId() {
        return valueFieldId;
    }

    public void setValueFieldId(String valueFieldId) {
        this.valueFieldId = valueFieldId;
    }

    public void setCompiled(boolean compiled) {
        this.compiled = compiled;
    }

    @Override
    public String getSrc() {
        return null;
    }

}
