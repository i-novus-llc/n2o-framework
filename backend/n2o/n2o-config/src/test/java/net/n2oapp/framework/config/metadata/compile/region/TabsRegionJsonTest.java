package net.n2oapp.framework.config.metadata.compile.region;


import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.page.SimplePageElementIOv2;
import net.n2oapp.framework.config.io.page.StandardPageElementIOv2;
import net.n2oapp.framework.config.io.region.TabsRegionIOv1;
import net.n2oapp.framework.config.io.widget.HtmlWidgetElementIOv4;
import net.n2oapp.framework.config.metadata.compile.page.StandardPageCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.HtmlWidgetCompiler;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирование маппинга java модели в json для региона в виде вкладок
 */
public class TabsRegionJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.ios(new SimplePageElementIOv2(), new StandardPageElementIOv2(), new TabsRegionIOv1(), new HtmlWidgetElementIOv4())
                .compilers(new StandardPageCompiler(), new TabsRegionCompiler(), new HtmlWidgetCompiler());
    }


    @Test
    public void tabsRegion() {
        check("net/n2oapp/framework/config/mapping/testTabsRegionJson.page.xml",
                "components/regions/Tabs/TabsRegions.meta.json")
                .cutXml("regions.single[0]")
                .exclude("tabs[0].fetchOnInit", "tabs[0].opened", "tabs[0].widgetId", "tabs[1]")
                .assertEquals();

    }

}
