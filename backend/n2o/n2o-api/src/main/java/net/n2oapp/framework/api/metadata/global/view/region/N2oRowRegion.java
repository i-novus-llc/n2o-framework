package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель региона {@code <row>}
 */
@Getter
@Setter
public class N2oRowRegion extends N2oRegion {
    private Integer columns;
    private Boolean wrap;
    private AlignEnum align;
    private JustifyEnum justify;

    @Override
    public String getAlias() {
        return "row";
    }
}