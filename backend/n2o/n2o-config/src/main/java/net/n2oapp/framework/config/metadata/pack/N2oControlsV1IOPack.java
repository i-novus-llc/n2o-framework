package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;

/**
 * Набор ридеров и персистеров n2o-control-1.0
 */
@Deprecated //use V2
public class N2oControlsV1IOPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oControlsV1ReadersPack(),
                new N2oControlsV1PersistersPack());
    }
}
