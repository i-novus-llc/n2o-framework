package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель региона с горизонтальным делителем.
 */
@Getter
@Setter
public class N2oLineRegion extends N2oRegion {
    private String name;
    private Boolean collapsible;

    @Override
    public String getAlias() {
        return "line";
    }
}
