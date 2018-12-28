package net.n2oapp.framework.api.metadata.global.view.widget.table;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

import java.io.Serializable;

/**
 * User: iryabov
 * Date: 04.03.14
 * Time: 12:12
 */
public class N2oRow implements Serializable, NamespaceUriAware {
    N2oSwitch color;
    private String colorFieldId;
    private String namespaceUri;

    public N2oSwitch getColor() {
        return color;
    }

    public void setColor(N2oSwitch color) {
        this.color = color;
    }

    public String getColorFieldId() {
        return colorFieldId;
    }

    public void setColorFieldId(String colorFieldId) {
        this.colorFieldId = colorFieldId;
    }

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }
    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }
}
