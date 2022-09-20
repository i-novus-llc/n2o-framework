package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.aware.CssClassAware;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

import java.util.Map;

/**
 * Абстрактная ячейка
 */
@Getter
@Setter
public abstract class N2oAbstractCell implements N2oCell, ExtensionAttributesAware, CssClassAware {
    private String id;
    private String src;
    private String namespaceUri;
    private String cssClass;
    private String style;
    private String visible;
    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;
}
