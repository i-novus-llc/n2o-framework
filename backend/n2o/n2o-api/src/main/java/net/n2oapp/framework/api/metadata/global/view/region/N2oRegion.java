package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.aware.SrcAware;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.Map;

/**
 * Модель региона 1.0
 */
@Getter
@Setter
public abstract class N2oRegion implements Source, IdAware, SrcAware, NamespaceUriAware, SourceMetadata, ExtensionAttributesAware {
    /**
     * @deprecated
     */
    @Deprecated
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
    private String id;
    private String place;
    private String src;
    private N2oWidget[] widgets;
    private String namespaceUri;
    private String className;
    Map<N2oNamespace, Map<String, String>> extAttributes;

    @Override
    public String getPostfix() {
        return "region";
    }

    public String getWidgetPrefix() {
        return "w";
    }
}
