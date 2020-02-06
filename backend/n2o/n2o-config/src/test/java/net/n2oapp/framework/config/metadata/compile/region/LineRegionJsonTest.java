package net.n2oapp.framework.config.metadata.compile.region;


import net.n2oapp.framework.config.io.page.SimplePageElementIOv2;
import net.n2oapp.framework.config.io.page.StandardPageElementIOv2;
import net.n2oapp.framework.config.io.region.LineRegionIOv1;
import net.n2oapp.framework.config.io.widget.HtmlWidgetElementIOv4;
import net.n2oapp.framework.config.metadata.compile.page.StandardPageCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.HtmlWidgetCompiler;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирвоание маппинга java модели в json для простого региона
 */
public class LineRegionJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        builder.ios(new SimplePageElementIOv2(), new StandardPageElementIOv2(), new LineRegionIOv1(), new HtmlWidgetElementIOv4())
                .compilers(new LineRegionCompiler(), new StandardPageCompiler(), new HtmlWidgetCompiler());
    }


    @Test
    public void lineRegion() {
        check("net/n2oapp/framework/config/mapping/testLineRegionJson.page.xml",
                "components/regions/List/ListRegion.meta.json")
                .cutXml("regions.single[0]")
                .changeValue("items[0].widgetId", "testLineRegionJson_html")
                .exclude("items[0].widgetId", "items[0].opened", "items[1]", "items[0].id")
                        .assertEquals();
    }

}
