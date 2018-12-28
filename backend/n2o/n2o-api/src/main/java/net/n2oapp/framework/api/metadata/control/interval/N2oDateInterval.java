package net.n2oapp.framework.api.metadata.control.interval;

import net.n2oapp.framework.api.metadata.control.properties.PopupPlacement;

/**
 * Компонент ввода интервала дат
 */
public class N2oDateInterval extends N2oIntervalField {

    public static final String INTERVAL_CHECK_FUNC = "checkDateInterval(%s.begin, %s.end, '%s')";
    public static final String WRONG_INTERVAL_MSG = "Дата начала не должна быть больше даты окончания";

    private PopupPlacement popupPlacement;

    private String dateFormat;
    private String beginDefaultTime;
    private String endDefaultTime;
    private Boolean utc;

    public N2oDateInterval() {
    }

    public N2oDateInterval(String id) {
        setId(id);
    }

    public String getDefaultSrc() {
        return "n2o/controls/date.picker/date.interval";
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getBeginDefaultTime() {
        return beginDefaultTime;
    }

    public void setBeginDefaultTime(String beginDefaultTime) {
        this.beginDefaultTime = beginDefaultTime;
    }

    public String getEndDefaultTime() {
        return endDefaultTime;
    }

    public void setEndDefaultTime(String endDefaultTime) {
        this.endDefaultTime = endDefaultTime;
    }

    public Boolean getUtc() {
        return utc;
    }

    public void setUtc(Boolean utc) {
        this.utc = utc;
    }

    public PopupPlacement getPopupPlacement() {
        return popupPlacement;
    }

    public void setPopupPlacement(PopupPlacement popupPlacement) {
        this.popupPlacement = popupPlacement;
    }

}
