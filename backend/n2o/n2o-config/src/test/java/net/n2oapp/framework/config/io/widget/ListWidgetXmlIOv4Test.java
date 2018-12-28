package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.io.widget.table.cell.*;
import net.n2oapp.framework.config.reader.control.N2oClassifierXmlReaderV1;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

public class ListWidgetXmlIOv4Test {
    @Test
    public void testListWidgetXmlIOv4Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader()
                        .addControlReader()
                        .addReader(new ListWidgetElementIOv4())
                        .addReader(new TextCellElementIOv2()))
                .addPersister(new SelectiveStandardPersister()
                        .addControlPersister()
                        .addPersister(new ListWidgetElementIOv4())
                        .addPersister(new TextCellElementIOv2()));

        assert tester.check("net/n2oapp/framework/config/io/widget/list/testWidgetListIOv4.widget.xml");
    }
}
