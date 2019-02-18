package net.n2oapp.framework.api.metadata.global.view.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.chart.Display;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oChartValue;

public class N2oChart extends N2oWidget {

    private Display display;
    private N2oChartValue[] values;
    private String labelFieldId;
    private String valueFieldId;

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public N2oChartValue[] getValues() {
        return values;
    }

    public void setValues(N2oChartValue[] values) {
        this.values = values;
    }

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
}
