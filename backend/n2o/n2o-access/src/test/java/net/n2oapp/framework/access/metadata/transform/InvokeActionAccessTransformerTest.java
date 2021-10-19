package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.ToolbarAccessTransformer;
import net.n2oapp.framework.access.integration.metadata.transform.action.InvokeActionAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersPack;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertTrue;

public class InvokeActionAccessTransformerTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oObjectsPack(), new AccessSchemaPack(), new N2oDataProvidersPack())
                .sources(new CompileInfo("net/n2oapp/framework/access/metadata/transform/testToolbarAccessTransformer.object.xml"),
                        new CompileInfo("net/n2oapp/framework/access/metadata/transform/testObjectAccessTransformer.object.xml"))
                .transformers(new ToolbarAccessTransformer(), new InvokeActionAccessTransformer());
    }

    @Test
    public void testInvokeTransform() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testInvoke");

        ReadCompileTerminalPipeline<?> pipeline = compile("net/n2oapp/framework/access/metadata/schema/testInvoke.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testInvokeActionAccessTransformer.page.xml");
        StandardPage page = (StandardPage) pipeline.transform().get(new PageContext("testInvokeActionAccessTransformer"));

        Security.SecurityObject securityObject = ((Security) page.getToolbar().get("bottomRight")
                .get(0).getButtons().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object");
        assertThat(securityObject.getRoles().size(), is(1));
        assertThat(securityObject.getRoles().stream().findFirst().get(), is("role"));
        assertThat(securityObject.getPermissions(), nullValue());
        assertThat(securityObject.getUsernames(), nullValue());

        securityObject = ((Security) page.getToolbar().getButton("update")
                .getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object");
        assertThat(securityObject.getRoles().size(), is(1));
        assertThat(securityObject.getRoles().stream().findFirst().get(), is("role"));
        assertThat(securityObject.getPermissions(), nullValue());
        assertThat(securityObject.getUsernames(), nullValue());
    }

    @Test
    public void testInvokeTransformV2() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testInvokeV2");

        ReadCompileTerminalPipeline<?> pipeline = compile("net/n2oapp/framework/access/metadata/schema/testInvokeV2.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testInvokeActionAccessTransformer.page.xml");
        StandardPage page = (StandardPage) pipeline.transform().get(new PageContext("testInvokeActionAccessTransformer"));

        Security.SecurityObject securityObject = ((Security) page.getToolbar().get("bottomRight")
                .get(0).getButtons().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object");
        assertThat(securityObject.getRoles().size(), is(1));
        assertThat(securityObject.getRoles().stream().findFirst().get(), is("role"));
        assertThat(securityObject.getPermissions(), nullValue());
        assertThat(securityObject.getUsernames(), nullValue());
        assertTrue(securityObject.getAnonymous());

        securityObject = ((Security) page.getToolbar().getButton("update")
                .getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object");
        assertThat(securityObject.getRoles().size(), is(1));
        assertThat(securityObject.getRoles().stream().findFirst().get(), is("role"));
        assertThat(securityObject.getPermissions(), nullValue());
        assertThat(securityObject.getUsernames(), nullValue());
        assertTrue(securityObject.getAnonymous());

    }
}