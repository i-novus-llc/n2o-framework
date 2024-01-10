package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;

/**
 * Исходная модель строки филдсета
 */
@Getter
@Setter
public class N2oFieldsetRow extends net.n2oapp.framework.api.metadata.control.N2oComponent {
    private SourceComponent[] items;
}
