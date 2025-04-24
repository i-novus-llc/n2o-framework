package net.n2oapp.framework.api;

import org.jdom2.Namespace;

import java.io.Serializable;

public class N2oNamespace implements Serializable {

    private String prefix;
    private String uri;

    public N2oNamespace(Namespace namespace) {
        this.prefix = namespace.getPrefix();
        this.uri = namespace.getURI();
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (ob instanceof N2oNamespace namespace) {
            return uri.equals(namespace.getUri());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }
}
