package net.n2oapp.framework.api.metadata.dataprovider;

public class AbstractDataProvider implements DataProvider {
    private String namespaceUri;

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }
}
