package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Модель региона с горизонтальным делителем
 */
@Getter
@Setter
@VisualComponent
public class N2oLineRegion extends N2oRegion {
    @VisualAttribute
    private String label;
    @VisualAttribute
    private Boolean collapsible;
    @VisualAttribute
    private Boolean hasSeparator;
    @VisualAttribute
    private Boolean expand;

    @Override
    public String getAlias() {
        return "line";
    }
}
