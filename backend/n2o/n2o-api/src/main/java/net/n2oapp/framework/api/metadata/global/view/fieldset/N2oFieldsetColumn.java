package net.n2oapp.framework.api.metadata.global.view.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

/**
 * Исходная модель столбца филдсета
 */
public class N2oFieldsetColumn implements Source, NamespaceUriAware {
    private String classRow;
    private Integer size;
    private String namespaceUri;
    private NamespaceUriAware[] items;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

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
