package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель региона {@code <col>}
 */
@Getter
@Setter
public class N2oColRegion extends N2oRegion {
    private Integer size;
    private Integer offset;

    @Override
    public String getAlias() {
        return "col";
    }
}