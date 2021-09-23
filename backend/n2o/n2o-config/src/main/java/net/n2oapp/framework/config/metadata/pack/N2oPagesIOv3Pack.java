package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.ReadersBuilder;
import net.n2oapp.framework.config.io.page.v3.*;

/**
 * Набор считывателей страниц версии 3.0
 */
public class N2oPagesIOv3Pack implements MetadataPack<ReadersBuilder> {
    @Override
    public void build(ReadersBuilder b) {
        b.ios(new SimplePageElementIOv3(),
                new StandardPageElementIOv3(),
                new LeftRightPageElementIOv3(),
                new TopLeftRightPageElementIOv3(),
                new SearchablePageElementIOv3());
    }
}
