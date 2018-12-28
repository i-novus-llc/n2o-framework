package net.n2oapp.framework.config.metadata.compile.region;


import net.n2oapp.framework.config.io.page.SimplePageElementIOv2;
import net.n2oapp.framework.config.io.page.StandardPageElementIOv2;
import net.n2oapp.framework.config.io.region.NoneRegionIOv1;
import net.n2oapp.framework.config.io.widget.HtmlWidgetElementIOv4;
import net.n2oapp.framework.config.metadata.compile.page.SimplePageCompiler;
import net.n2oapp.framework.config.metadata.compile.region.NoneRegionCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.HtmlWidgetCompiler;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

/**
 * Тестирвоание маппинга java модели в json для простого региона
 */
public class NoneRegionJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        builder.ios(new SimplePageElementIOv2(), new StandardPageElementIOv2(), new NoneRegionIOv1(), new HtmlWidgetElementIOv4())
                .compilers(new NoneRegionCompiler(), new SimplePageCompiler(), new HtmlWidgetCompiler());
    }

    @Test
    public void noneRegion() {
        check("net/n2oapp/framework/config/mapping/testNoneRegionJson.page.xml",
                "components/regions/None/NoneRegion.meta.json")
                .cutXml("layout.regions.single[0]")
                .exclude("items[0].widgetId",
                        "items[0].id")
                .assertEquals();
    }

}
