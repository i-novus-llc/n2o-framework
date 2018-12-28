package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.config.io.widget.table.cell.TextCellElementIOv2;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

public class HtmlWidgetXmlIOv4Test {
    @Test
    public void testHtmlWidgetXmlIOv4Test(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addReader(new HtmlWidgetElementIOv4()).addReader(new TextCellElementIOv2()) )
                .addPersister(new SelectiveStandardPersister().addPersister(new HtmlWidgetElementIOv4()).addPersister(new TextCellElementIOv2()));

        assert tester.check("net/n2oapp/framework/config/io/widget/html/testHtmlWidgetIOv4.widget.xml");
    }
}
