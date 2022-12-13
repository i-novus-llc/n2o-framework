package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Исходная модель филдсета с горизонтальной линией.
 */
@Getter
@Setter
@VisualComponent
public class N2oLineFieldSet extends N2oFieldSet {
    @VisualAttribute
    private Boolean collapsible;
    @VisualAttribute
    private Boolean hasSeparator;
    @VisualAttribute
    private Boolean expand;
}
