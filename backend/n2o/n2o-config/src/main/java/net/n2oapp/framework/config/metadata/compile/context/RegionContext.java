package net.n2oapp.framework.config.metadata.compile.context;

import net.n2oapp.framework.api.metadata.global.view.region.N2oAbstractRegion;
import net.n2oapp.framework.api.metadata.meta.region.Region;

public class RegionContext extends BaseCompileContext<Region, N2oAbstractRegion> {

    public RegionContext(String regionId) {
        super(regionId, N2oAbstractRegion.class, Region.class);
    }
}
