package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.region.v2.CustomRegionIOv2;
import net.n2oapp.framework.config.io.region.v2.LineRegionIOv2;
import net.n2oapp.framework.config.io.region.v2.PanelRegionIOv2;
import net.n2oapp.framework.config.io.region.v2.TabsRegionIOv2;

public class N2oRegionsV2IOPack implements MetadataPack<XmlIOBuilder> {
    @Override
    public void build(XmlIOBuilder b) {
        b.ios(new LineRegionIOv2(),
                new PanelRegionIOv2(),
                new TabsRegionIOv2(),
                new CustomRegionIOv2());
    }
}
