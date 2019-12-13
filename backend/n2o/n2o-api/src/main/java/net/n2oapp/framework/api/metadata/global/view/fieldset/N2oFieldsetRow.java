package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.CssClassAware;

/**
 * Исходная модель строки филдсета
 */
@Getter
@Setter
public class N2oFieldsetRow implements SourceComponent, CssClassAware {
    private String cssClass;
    private String style;
    private String namespaceUri;
    private SourceComponent[] items;
}
