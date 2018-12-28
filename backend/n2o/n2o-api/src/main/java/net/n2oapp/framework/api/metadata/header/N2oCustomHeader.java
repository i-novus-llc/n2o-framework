package net.n2oapp.framework.api.metadata.header;

/**
 * Настраиваемый хедер
 */
public class N2oCustomHeader extends N2oHeader {

    private String src;

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrc() {
        return src;
    }
}
