package net.n2oapp.framework.api.metadata.global.view.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

/**
 * Исходная модель строки филдсета
 */
public class N2oFieldsetRow implements Source, NamespaceUriAware {
    private String classRow;
    private String namespaceUri;
    private NamespaceUriAware[] items;

    public NamespaceUriAware[] getItems() {
        return items;
    }

    public void setItems(NamespaceUriAware[] items) {
        this.items = items;
    }

    public String getClassRow() {
        return classRow;
    }

    public void setClassRow(String classRow) {
        this.classRow = classRow;
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
