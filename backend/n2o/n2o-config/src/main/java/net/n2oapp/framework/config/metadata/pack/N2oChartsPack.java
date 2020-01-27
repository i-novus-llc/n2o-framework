package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.widget.chart.AreaChartCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.chart.BarChartCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.chart.LineChartCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.chart.PieChartCompiler;

/**
 * Набор для компиляции графиков/диаграмм
 */
public class N2oChartsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oChartsIOPack());
        b.compilers(
                new AreaChartCompiler(),
                new BarChartCompiler(),
                new LineChartCompiler(),
                new PieChartCompiler()
        );
    }
}
