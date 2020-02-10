package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.ReadersBuilder;
import net.n2oapp.framework.config.io.fieldset.*;
import net.n2oapp.framework.config.reader.fieldset.FieldSetXmlReaderV2;
import net.n2oapp.framework.config.reader.fieldset.FieldSetXmlReaderV3;

/**
 * Набор считывателей филдсетов
 */
public class N2oFieldSetsIOPack implements MetadataPack<ReadersBuilder> {
    @Override
    public void build(ReadersBuilder b) {
        b.ios(new SetFieldsetElementIOv4(),
                new LineFieldsetElementIOv4(),
                new MultiFieldsetElementIOv4(),
                new ColElementIO4(),
                new RowElementIO4());
        b.readers(new FieldSetXmlReaderV3(), new FieldSetXmlReaderV2());
    }
}
