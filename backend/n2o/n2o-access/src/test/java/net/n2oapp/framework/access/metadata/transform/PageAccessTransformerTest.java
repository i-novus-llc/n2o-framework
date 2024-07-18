package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.PageAccessTransformer;
import net.n2oapp.framework.access.integration.metadata.transform.WidgetAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityObject;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.LineRegion;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.region.TabsRegion;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование трансформации доступа страницы
 */
class PageAccessTransformerTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/access/metadata/transform/testQuery.query.xml"),
                new CompileInfo("net/n2oapp/framework/access/metadata/transform/testObjectAccessTransformer.object.xml"))
                .packs(new N2oAllPagesPack(), new AccessSchemaPack(), new N2oAllDataPack())
                .transformers(new PageAccessTransformer(), new WidgetAccessTransformer());
    }

    @Test
    void testPageV2() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testPageV2");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testPageV2.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testPageV2AccessTransformer.page.xml");

        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testPageV2AccessTransformer"));
        SecurityObject regionSecurityObject = ((Security) ((Widget) page.getRegions().get("single").get(0).getContent().get(0))
                .getProperties()
                .get(SECURITY_PROP_NAME))
                .get(0)
                .get("object");

        SecurityObject widgetSecurityObject = ((Security) ((Widget) page.getRegions().get("single").get(0).getContent().get(0))
                .getProperties()
                .get(SECURITY_PROP_NAME)).get(0).get("object");
        assertThat(regionSecurityObject, equalTo(widgetSecurityObject));

        SecurityObject pageSecurityObject = ((Security) page.getProperties().get(SECURITY_PROP_NAME)).get(0).get("page");
        assertThat(pageSecurityObject.getRoles(), nullValue());
        assertThat(pageSecurityObject.getPermissions().size(), is(1));
        assertThat(pageSecurityObject.getUsernames(), nullValue());
        assertThat(pageSecurityObject.getAnonymous(), nullValue());
        assertThat(pageSecurityObject.getAuthenticated(), nullValue());
        assertThat(pageSecurityObject.getPermitAll(), nullValue());
    }

    @Test
    void testPageV3() {
        ReadCompileTerminalPipeline pipeline = compile(
                "net/n2oapp/framework/access/metadata/default.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testPageV3AccessTransformer.page.xml");
        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testPageV3AccessTransformer"));

        List<Region> regions = page.getRegions().get("single");

        // without properties
        assertThat(regions.get(0).getProperties(), nullValue());

        List<TabsRegion.Tab> tabItems = ((TabsRegion) regions.get(1)).getItems();
        // if region has access attributes
        SecurityObject secProps = getSecurityProperties(tabItems.get(0));
        assertThat(secProps.getAuthenticated(), is(true));
        assertThat(secProps.getRoles(), nullValue());
        // if region has no access attributes
        assertThat(tabItems.get(1).getProperties(), nullValue());

        // region with admin role (from widget) and create permission (from nested region)
        LineRegion lineRegion = (LineRegion) regions.get(2);
        secProps = getSecurityProperties(lineRegion);
        assertThat(secProps.getRoles().size(), is(1));
        assertThat(secProps.getRoles().contains("admin"), is(true));

        // line for all
        assertThat(regions.get(3).getProperties(), nullValue());
    }

    private SecurityObject getSecurityProperties(PropertiesAware item) {
        return ((Security) item.getProperties().get(SECURITY_PROP_NAME)).get(0).get("custom");
    }
}
