package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.io.toolbar.v2.SubmenuIOv2;
import net.n2oapp.framework.config.io.widget.v5.*;

public class N2oWidgetsV5IOPack implements MetadataPack<XmlIOBuilder> {
    @Override
    public void build(XmlIOBuilder b) {
        b.ios(new FormElementIOV5(),
                new TableElementIOV5(),
                new ListWidgetElementIOv5(),
                new HtmlWidgetElementIOv5(),
                new CustomWidgetIOv5(),
                new TreeElementIOv5(),
                new ChartWidgetIOv5(),
                new CalendarWidgetIOv5(),
                new ButtonIOv2(),
                new SubmenuIOv2(),
                new TilesWidgetIOV5(),
                new CardsWidgetIOV5());
    }
}