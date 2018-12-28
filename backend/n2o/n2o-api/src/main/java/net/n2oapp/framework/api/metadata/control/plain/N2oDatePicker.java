package net.n2oapp.framework.api.metadata.control.plain;

/**
 * Компонент ввода даты
 */
public class N2oDatePicker extends N2oPlainField {
    private String dateFormat;
    private String defaultTime;
    private String max;
    private String min;
    private Boolean utc;

    public String getDefaultSrc() {
        return "n2o/controls/date.picker/date.picker";
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }


    public String getDefaultTime() {
        return defaultTime;
    }

    public void setDefaultTime(String defaultTime) {
        this.defaultTime = defaultTime;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public Boolean getUtc() {
        return utc;
    }

    public void setUtc(Boolean utc) {
        this.utc = utc;
    }
}
