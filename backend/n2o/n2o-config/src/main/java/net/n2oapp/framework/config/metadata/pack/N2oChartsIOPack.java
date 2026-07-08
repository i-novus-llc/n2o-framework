package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.widget.charts.AreaChartIOv5;
import net.n2oapp.framework.config.io.widget.charts.BarChartIOv5;
import net.n2oapp.framework.config.io.widget.charts.LineChartIOv5;
import net.n2oapp.framework.config.io.widget.charts.PieChartIOv5;

/**
 * Набор считывателей графиков/диаграмм
 */
public class N2oChartsIOPack implements MetadataPack<XmlIOBuilder<?>> {
    @Override
    public void build(XmlIOBuilder<?> b) {
        b.ios(new AreaChartIOv5(),
                new BarChartIOv5(),
                new LineChartIOv5(),
                new PieChartIOv5());
    }
}
