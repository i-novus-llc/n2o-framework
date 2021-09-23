package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.toolbar.SubmenuIO;
import net.n2oapp.framework.config.io.widget.*;
import net.n2oapp.framework.config.io.widget.chart.ChartWidgetIOv4;
import net.n2oapp.framework.config.io.widget.form.FormElementIOV4;
import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;

public class N2oWidgetsIOPack implements MetadataPack<XmlIOBuilder> {
    @Override
    public void build(XmlIOBuilder b) {
        b.ios(new FormElementIOV4(),
                new TableElementIOV4(),
                new ListWidgetElementIOv4(),
                new HtmlWidgetElementIOv4(),
                new CustomWidgetIOv4(),
                new TreeElementIOv4(),
                new ChartWidgetIOv4(),
                new CalendarWidgetIOv4(),
                new ButtonIO(),
                new SubmenuIO(),
                new TilesWidgetIOV4(),
                new CardsWidgetIOV4());
    }
}