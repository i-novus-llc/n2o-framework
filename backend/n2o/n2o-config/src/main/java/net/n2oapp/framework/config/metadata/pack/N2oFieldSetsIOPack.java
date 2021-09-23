package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.fieldset.*;

/**
 * Набор считывателей филдсетов
 */
public class N2oFieldSetsIOPack implements MetadataPack<XmlIOBuilder> {
    @Override
    public void build(XmlIOBuilder b) {
        b.ios(new SetFieldsetElementIOv4(),
                new LineFieldsetElementIOv4(),
                new MultiFieldsetElementIOv4(),
                new ColElementIO4(),
                new RowElementIO4());
    }
}
