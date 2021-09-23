package net.n2oapp.framework.access.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;

public class AccessPointsV1Pack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new AccessPointsV1ReadersPack());
    }
}
