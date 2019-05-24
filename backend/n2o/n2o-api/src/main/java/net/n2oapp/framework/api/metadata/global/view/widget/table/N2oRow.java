package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

import java.io.Serializable;

/**
 * User: iryabov
 * Date: 04.03.14
 * Time: 12:12
 */
@Getter
@Setter
public class N2oRow implements Serializable, NamespaceUriAware {
    private N2oSwitch color;
    private N2oRowClick rowClick;
    private String rowClass;
    private String namespaceUri;

}
