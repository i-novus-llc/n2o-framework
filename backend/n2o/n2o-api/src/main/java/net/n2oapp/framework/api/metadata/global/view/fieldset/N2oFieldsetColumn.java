package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.CssClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

/**
 * Исходная модель столбца филдсета
 */
@Getter
@Setter
public class N2oFieldsetColumn implements Source, CssClassAware, NamespaceUriAware {
    private String cssClass;
    private String style;
    private Integer size;
    private String namespaceUri;
    private NamespaceUriAware[] items;
}
