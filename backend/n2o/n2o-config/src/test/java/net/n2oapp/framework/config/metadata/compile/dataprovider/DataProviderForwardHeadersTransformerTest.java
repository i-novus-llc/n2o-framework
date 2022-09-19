package net.n2oapp.framework.config.metadata.compile.dataprovider;

import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.pipeline.ReadTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class DataProviderForwardHeadersTransformerTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/transformer/dataProviderForwardHeadersTransformer.query.xml"))
                .packs(new N2oAllDataPack(), new N2oAllPagesPack())
                .transformers(new DataProviderForwardHeadersTransformer());
        builder.propertySources("application-test.properties");
    }

    @Test
    public void testDataProviderForwardHeadersTransformer() {
        ReadTerminalPipeline pipeline =
                read("net/n2oapp/framework/config/metadata/transformer/dataProviderForwardHeadersTransformer.query.xml").transform();

        N2oQuery query = (N2oQuery) pipeline.get("dataProviderForwardHeadersTransformer", N2oQuery.class);
        assertThat(((N2oRestDataProvider) query.getLists()[0].getInvocation()).getForwardedHeadersSet().size(), is(3));
        assertTrue(((N2oRestDataProvider) query.getLists()[0].getInvocation()).getForwardedHeadersSet().contains("testHeader1"));
        assertTrue(((N2oRestDataProvider) query.getLists()[0].getInvocation()).getForwardedHeadersSet().contains("testHeader2"));
        assertTrue(((N2oRestDataProvider) query.getLists()[0].getInvocation()).getForwardedHeadersSet().contains("testHeader3"));
    }
}
