package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель региона с горизонтальным делителем
 */
@Getter
@Setter
public class N2oLineRegion extends N2oRegion {
    private String label;
    private Boolean collapsible;
    private Boolean hasSeparator;
    private Boolean expand;

    @Override
    public String getAlias() {
        return "line";
    }
}
