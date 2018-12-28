package net.n2oapp.framework.api.metadata.global.view.action;

/**
* User: operehod
* Date: 02.12.2014
* Time: 13:07
*/
public enum LabelType {
    text, icon, textAndIcon;

    public boolean isHasIcon() {
        return !this.equals(text);
    }

    public boolean isHasText() {
        return !this.equals(icon);
    }
}
