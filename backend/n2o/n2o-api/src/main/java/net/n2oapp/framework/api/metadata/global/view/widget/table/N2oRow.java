package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;


@Getter
@Setter
public class N2oRow implements Source, NamespaceUriAware {
    private String src;
    private N2oSwitch color;
    private N2oRowClick rowClick;
    private N2oRowOverlay rowOverlay;
    private String rowClass;
    private String style;
    private String namespaceUri;
}
