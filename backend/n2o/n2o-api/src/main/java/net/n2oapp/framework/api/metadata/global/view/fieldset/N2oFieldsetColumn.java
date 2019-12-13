package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.CssClassAware;

/**
 * Исходная модель столбца филдсета
 */
@Getter
@Setter
public class N2oFieldsetColumn implements SourceComponent, CssClassAware {
    private String cssClass;
    private Integer size;
    private String namespaceUri;
    private SourceComponent[] items;
}
