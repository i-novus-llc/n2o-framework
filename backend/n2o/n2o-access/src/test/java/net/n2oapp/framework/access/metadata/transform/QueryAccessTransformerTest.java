package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.QueryAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class QueryAccessTransformerTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new AccessSchemaPack())
                .sources(new CompileInfo("net/n2oapp/framework/access/metadata/transform/testObjectAccessTransformer.object.xml"))
                .transformers(new QueryAccessTransformer());
    }


    @Test
    public void testQueryTransform() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testQuery");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testQuery.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testQueryAccessTransformer.query.xml");

        CompiledQuery query = (CompiledQuery) ((ReadCompileTerminalPipeline) pipeline.transform()).get(new QueryContext("testQueryAccessTransformer"));
        assertThat(((Security) query.getProperties().get("security")).getSecurityMap().get("object").getRoles().contains("role"), is(true));
        assertThat(((Security) query.getProperties().get("security")).getSecurityMap().get("object").getUsernames().contains("user"), is(true));
        assertThat(((Security) query.getProperties().get("security")).getSecurityMap().get("object").getPermissions(), nullValue());
    }

    @Test
    public void testQueryTransformV2() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testQueryV2");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testQueryV2.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testQueryAccessTransformer.query.xml");

        CompiledQuery query = (CompiledQuery) ((ReadCompileTerminalPipeline) pipeline.transform()).get(new QueryContext("testQueryAccessTransformer"));
        assertThat(((Security) query.getProperties().get("security")).getSecurityMap().get("object").getRoles().contains("role"), is(true));
        assertThat(((Security) query.getProperties().get("security")).getSecurityMap().get("object").getUsernames().contains("user"), is(true));
        assertThat(((Security) query.getProperties().get("security")).getSecurityMap().get("object").getPermissions(), nullValue());
        assertThat(((Security) query.getProperties().get("security")).getSecurityMap().get("object").getAnonymous(), is(true));
    }
}