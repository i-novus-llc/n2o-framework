package net.n2oapp.framework.api.metadata.global.view.action.control;

import java.io.Serializable;

/**
 * User: iryabov
 * Date: 11.09.13
 * Time: 14:39
 */
public class N2oMapping implements Serializable {
    private String fieldId;
    private String paramName;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}
