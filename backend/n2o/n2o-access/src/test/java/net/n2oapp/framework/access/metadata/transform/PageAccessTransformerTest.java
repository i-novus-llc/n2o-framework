package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.PageAccessTransformer;
import net.n2oapp.framework.access.integration.metadata.transform.WidgetAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PageAccessTransformerTest extends SourceCompileTestBase {
    @Override
    @Before
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
    public void testRegion() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testRegion");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testRegion.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testRegionAccessTransformer.page.xml");

        Page page = (Page) ((ReadCompileTerminalPipeline) pipeline.transform()).get(new PageContext("testRegionAccessTransformer"));
        Security.SecurityObject regionSecurityObject = ((Security) page.getLayout().getRegions().get("single").get(0).getItems().get(0)
                .getProperties()
                .get(SECURITY_PROP_NAME))
                .getSecurityMap()
                .get("object");

        Security.SecurityObject widgetSecurityObject = ((Security) page.getWidgets()
                .get("testRegionAccessTransformer_testTable").getProperties()
                .get(SECURITY_PROP_NAME)).getSecurityMap().get("object");
        assertThat(regionSecurityObject, equalTo(widgetSecurityObject));
    }

    @Test
    public void testRegionV2() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testRegionV2");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testRegionV2.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testRegionAccessTransformer.page.xml");

        Page page = (Page) ((ReadCompileTerminalPipeline) pipeline.transform()).get(new PageContext("testRegionAccessTransformer"));
        Security.SecurityObject regionSecurityObject = ((Security) page.getLayout().getRegions().get("single").get(0).getItems().get(0)
                .getProperties()
                .get(SECURITY_PROP_NAME))
                .getSecurityMap()
                .get("object");

        Security.SecurityObject widgetSecurityObject = ((Security) page.getWidgets()
                .get("testRegionAccessTransformer_testTable").getProperties()
                .get(SECURITY_PROP_NAME)).getSecurityMap().get("object");
        assertThat(regionSecurityObject, equalTo(widgetSecurityObject));
    }
}
