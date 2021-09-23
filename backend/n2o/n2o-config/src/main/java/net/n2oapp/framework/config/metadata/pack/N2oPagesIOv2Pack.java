package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.page.*;

/**
 * Набор считывателей страниц версии 2.0
 */
public class N2oPagesIOv2Pack implements MetadataPack<XmlIOBuilder> {
    @Override
    public void build(XmlIOBuilder b) {
        b.ios(new SimplePageElementIOv2(),
                new StandardPageElementIOv2(),
                new LeftRightPageElementIOv2(),
                new TopLeftRightPageElementIOv2(),
                new SearchablePageElementIOv2());
    }
}
