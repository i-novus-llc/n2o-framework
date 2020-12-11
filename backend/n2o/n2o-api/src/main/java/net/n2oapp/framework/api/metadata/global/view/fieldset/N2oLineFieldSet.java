package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель филдсета с горизонтальной линией.
 */
@Getter
@Setter
public class N2oLineFieldSet extends N2oFieldSet {
    private Boolean collapsible;
    private Boolean hasSeparator;
    private Boolean expand;
}
