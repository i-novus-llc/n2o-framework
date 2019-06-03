package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.ReadersBuilder;
import net.n2oapp.framework.config.io.widget.CustomWidgetIOv4;
import net.n2oapp.framework.config.io.widget.HtmlWidgetElementIOv4;
import net.n2oapp.framework.config.io.widget.ListWidgetElementIOv4;
import net.n2oapp.framework.config.io.widget.TreeElementIOv4;
import net.n2oapp.framework.config.io.widget.form.FormElementIOV4;
import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.reader.widget.widget3.*;

public class N2oWidgetsIOPack implements MetadataPack<ReadersBuilder> {
    @Override
    public void build(ReadersBuilder b) {
        b.ios(new FormElementIOV4(),
                new TableElementIOV4(),
                new ListWidgetElementIOv4(),
                new HtmlWidgetElementIOv4(),
                new CustomWidgetIOv4(),
                new TreeElementIOv4());
        b.readers(new FormXmlReaderV3(),
                new TableXmlReaderV3(),
                new EditFormXmlReaderV3(),
                new HtmlWidgetXmlReaderV3(),
                new N2oChartReaderV3());
    }
}