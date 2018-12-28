package net.n2oapp.framework.api.metadata.global.view.widget.table.column;


import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;

import java.io.Serializable;

/**
 * User: operhod
 * Date: 30.01.14
 * Time: 13:50
 */
public abstract class AbstractColumn implements IdAware, Serializable{
    private String id;
    private String textFieldId;
    private String tooltipFieldId;
    private String width;
    private String format;
    private String labelName;
    private String labelIcon;
    private LabelType labelType;
    private Boolean visible;
    private String visibilityCondition;
    private String sortingFieldId;
    private DirectionType sortingDirection;


    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelIcon() {
        return labelIcon;
    }

    public void setLabelIcon(String labelIcon) {
        this.labelIcon = labelIcon;
    }

    public LabelType getLabelType() {
        return labelType;
    }

    public void setLabelType(LabelType labelType) {
        this.labelType = labelType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTooltipFieldId() {
        return tooltipFieldId;
    }

    public void setTooltipFieldId(String tooltipFieldId) {
        this.tooltipFieldId = tooltipFieldId;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public abstract boolean isDynamic();

    public String getVisibilityCondition() {
        return visibilityCondition;
    }

    public void setVisibilityCondition(String visibilityCondition) {
        this.visibilityCondition = visibilityCondition;
    }

    public String getSortingFieldId() {
        return sortingFieldId;
    }

    public void setSortingFieldId(String sortingFieldId) {
        this.sortingFieldId = sortingFieldId;
    }

    public DirectionType getSortingDirection() {
        return sortingDirection;
    }

    public void setSortingDirection(DirectionType sortingDirection) {
        this.sortingDirection = sortingDirection;
    }

    public String getTextFieldId() {
        return textFieldId;
    }

    public void setTextFieldId(String textFieldId) {
        this.textFieldId = textFieldId;
    }
}
