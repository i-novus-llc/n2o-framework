package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель региона 2.0
 */
@Getter
@Setter
public abstract class N2oRegion extends N2oAbstractRegion {
    private N2oAbstractRegion[] regions;
}
