package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import java.io.Serializable;

/**
 * @author V. Alexeev.
 * @date 02.03.2016
 */
public class N2oChartValue implements Serializable{

    private String fieldId;
    private String color;

    public N2oChartValue(String fieldID, String color) {
        this.fieldId = fieldID;
        this.color = color;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
