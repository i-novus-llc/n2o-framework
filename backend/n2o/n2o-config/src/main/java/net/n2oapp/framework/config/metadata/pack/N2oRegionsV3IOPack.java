package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.region.v3.*;

public class N2oRegionsV3IOPack implements MetadataPack<XmlIOBuilder> {
    @Override
    public void build(XmlIOBuilder b) {
        b.ios(new LineRegionIOv3(),
                new PanelRegionIOv3(),
                new TabsRegionIOv3(),
                new CustomRegionIOv3(),
                new ScrollspyRegionIOv3());
    }
}
