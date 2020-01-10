package net.n2oapp.framework.config.metadata.compile.chart;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.widget.chart.PieChartCompiler;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

public class PieChartJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oWidgetsPack());
        builder.compilers(new PieChartCompiler());
    }

    @Test
    public void pieChart() {
        check("net/n2oapp/framework/config/metadata/compile/chart/testPieChart.widget.xml",
//                "components/widgets/Chart/json/PieChart.meta.json")
                "components/widgets/Chart/json/test.meta.json")
                .assertEquals();

    }
}
