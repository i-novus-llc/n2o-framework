package net.n2oapp.framework.config.metadata.compile.context;

import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.meta.region.Region;

public class RegionContext extends BaseCompileContext<Region, N2oRegion> {

    public RegionContext(String regionId) {
        super(regionId, N2oRegion.class, Region.class);
    }
}
