package net.n2oapp.framework.config.metadata.compile.query;

import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersPack;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.test.SourceTransformTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест {@link TestEngineQueryTransformer}
 */
public class TestInvocationTransformTest extends SourceTransformTestBase {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oQueriesPack(), new N2oDataProvidersPack())
                .transformers(new TestEngineQueryTransformer());
    }

    @Test
    public void test() {
        N2oQuery query = transform("net/n2oapp/framework/config/metadata/compile/query/testTestInvocationTransformer.query.xml")
                .get("testTestInvocationTransformer", N2oQuery.class);
        assertThat(query.getFields()[0].getId(), is("id"));
        assertThat(query.getFields()[0].getSelectBody(), is(":expression"));
        assertThat(query.getFields()[0].getSortingBody(), is(":expression :idDirection"));
        assertThat(query.getFilters()[0].getFieldId(), is("id"));
        assertThat(query.getFilters()[0].getText(), is(":expression :eq :id"));
    }
}
