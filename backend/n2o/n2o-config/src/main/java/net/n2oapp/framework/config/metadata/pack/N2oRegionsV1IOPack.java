package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.region.*;

public class N2oRegionsV1IOPack implements MetadataPack<XmlIOBuilder> {
    @Override
    public void build(XmlIOBuilder b) {
        b.ios(new NoneRegionIOv1(),
                new LineRegionIOv1(),
                new PanelRegionIOv1(),
                new TabsRegionIOv1(),
                new CustomRegionIOv1());
    }
}
