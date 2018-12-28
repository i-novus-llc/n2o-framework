package net.n2oapp.framework.api.metadata.global.dao.invocation.model;

/**
 * Модель вызова sqlt запроса
 */
@Deprecated
public class N2oSqltInvocation implements N2oMapInvocation {

    private String file;
    private String blocks;
    private String namespaceUri;

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getBlocks() {
        return blocks;
    }

    public void setBlocks(String blocks) {
        this.blocks = blocks;
    }
}
