package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.aware.SrcAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;

import java.util.Map;

/**
 * Модель региона 2.0
 */
@Getter
@Setter
public abstract class N2oRegion extends N2oMetadata implements SrcAware, NamespaceUriAware,
        SourceMetadata, ExtensionAttributesAware {

    private String width;
    /**
     * @deprecated
     */
    @Deprecated
    private Map<String, Object> properties;
    /**
     * @deprecated
     */
    @Deprecated
    private String name;
    private String place;
    private String src;
    private String className;
    private String style;
    Map<N2oNamespace, Map<String, String>> extAttributes;
    private SourceComponent[] items;

    @Override
    public String getPostfix() {
        return "region";
    }

    public String getAlias() {
        return "w";
    }
}
