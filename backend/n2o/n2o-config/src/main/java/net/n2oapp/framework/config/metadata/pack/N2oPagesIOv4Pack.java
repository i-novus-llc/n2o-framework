package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;

/**
 * Набор считывателей страниц версии 4.0
 */
public class N2oPagesIOv4Pack implements MetadataPack<XmlIOBuilder> {
    @Override
    public void build(XmlIOBuilder b) {
        b.ios(new StandardPageElementIOv4());
    }
}
