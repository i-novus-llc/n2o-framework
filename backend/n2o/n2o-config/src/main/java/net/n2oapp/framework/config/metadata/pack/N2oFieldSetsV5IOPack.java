package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.fieldset.v5.*;

/**
 * Набор считывателей филдсетов
 */
public class N2oFieldSetsV5IOPack implements MetadataPack<XmlIOBuilder> {
    @Override
    public void build(XmlIOBuilder b) {
        b.ios(new SetFieldsetElementIOv5(),
                new LineFieldsetElementIOv5(),
                new MultiFieldsetElementIOv5(),
                new ColElementIO5(),
                new RowElementIO5());
    }
}
