package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.toolbar.SubmenuIO;
import net.n2oapp.framework.config.io.widget.v4.CardsWidgetIOV4;
import net.n2oapp.framework.config.io.widget.v4.TilesWidgetIOV4;
import net.n2oapp.framework.config.io.widget.v5.ChartWidgetIOv5;
import net.n2oapp.framework.config.io.widget.v5.FormElementIOV5;
import net.n2oapp.framework.config.io.widget.v5.*;
import net.n2oapp.framework.config.io.widget.v5.TableElementIOV5;

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
                new ButtonIO(),
                new SubmenuIO(),
                new TilesWidgetIOV4(),
                new CardsWidgetIOV4());
    }
}