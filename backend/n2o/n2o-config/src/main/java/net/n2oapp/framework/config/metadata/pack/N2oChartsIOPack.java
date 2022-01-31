package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.v4.charts.AreaChartIOv4;
import net.n2oapp.framework.config.io.widget.v4.charts.BarChartIOv4;
import net.n2oapp.framework.config.io.widget.v4.charts.LineChartIOv4;
import net.n2oapp.framework.config.io.widget.v4.charts.PieChartIOv4;
import net.n2oapp.framework.config.io.widget.v5.charts.AreaChartIOv5;
import net.n2oapp.framework.config.io.widget.v5.charts.BarChartIOv5;
import net.n2oapp.framework.config.io.widget.v5.charts.LineChartIOv5;
import net.n2oapp.framework.config.io.widget.v5.charts.PieChartIOv5;

/**
 * Набор считывателей графиков/диаграмм
 */
public class N2oChartsIOPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.ios(new AreaChartIOv4(),
                new BarChartIOv4(),
                new LineChartIOv4(),
                new PieChartIOv4(),
                new AreaChartIOv5(),
                new BarChartIOv5(),
                new LineChartIOv5(),
                new PieChartIOv5());
    }
}
