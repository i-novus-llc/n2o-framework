package net.n2oapp.framework.api.metadata.global.view.widget.table;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * @author V. Alexeev.
 */
public class N2oDynamicSwitch<T> implements Serializable {

    private Map<String, T> cases;
    private T defaultCase;
    private String fieldId;
    private String valueFieldId;

    public Map<String, T> getCases() {
        return cases;
    }

    public Map<String, T> getCasesSafe() {
        return cases != null ? cases : Collections.emptyMap();
    }

    public void setCases(Map<String, T> cases) {
        this.cases = cases;
    }

    public T getDefaultCase() {
        return defaultCase;
    }

    public void setDefaultCase(T defaultCase) {
        this.defaultCase = defaultCase;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getValueFieldId() {
        return valueFieldId;
    }

    public void setValueFieldId(String valueFieldId) {
        this.valueFieldId = valueFieldId;
    }
}
