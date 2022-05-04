package net.n2oapp.framework.config.io.custom.prefix;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;

@Getter
@Setter
public class N2oTestModel extends N2oMetadata implements SourceComponent {

    private String schemaPrefix;

    @Override
    public String getPostfix() {
        return null;
    }

    @Override
    public void setNamespacePrefix(String namespacePrefix) {
        schemaPrefix = namespacePrefix;
    }

    @Override
    public String getNamespacePrefix() {
        return schemaPrefix;
    }

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public void setCssClass(String cssClass) {

    }

    @Override
    public String getStyle() {
        return null;
    }

    @Override
    public void setStyle(String style) {

    }

    @Override
    public String getSrc() {
        return null;
    }

    @Override
    public void setSrc(String src) {

    }
}
