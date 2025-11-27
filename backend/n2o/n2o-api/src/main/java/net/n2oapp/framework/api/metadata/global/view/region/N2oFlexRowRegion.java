package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель региона {@code <flex-row>}
 */
@Getter
@Setter
public class N2oFlexRowRegion extends N2oRegion {
    private Boolean wrap;
    private AlignEnum align;
    private JustifyEnum justify;

    @Override
    public String getAlias() {
        return "flex-row";
    }
}