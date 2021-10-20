package net.n2oapp.framework.access.metadata.pack;

import net.n2oapp.framework.access.metadata.accesspoint.io.ObjectAccessPointIOv2;
import net.n2oapp.framework.access.metadata.accesspoint.io.ObjectFiltersAccessPointIOv2;
import net.n2oapp.framework.access.metadata.accesspoint.io.PageAccessPointIOv2;
import net.n2oapp.framework.access.metadata.accesspoint.io.UrlAccessPointIOv2;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;

public class AccessPointsIOV2Pack implements MetadataPack<XmlIOBuilder> {
    @Override
    public void build(XmlIOBuilder b) {
        b.ios(new ObjectAccessPointIOv2(),
                new PageAccessPointIOv2(),
                new UrlAccessPointIOv2(),
                new ObjectFiltersAccessPointIOv2());
    }
}
