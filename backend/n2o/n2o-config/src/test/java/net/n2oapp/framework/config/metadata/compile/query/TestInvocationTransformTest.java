package net.n2oapp.framework.config.metadata.compile.query;

import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersPack;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.test.SourceTransformTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест {@link TestEngineQueryTransformer}
 */
class TestInvocationTransformTest extends SourceTransformTestBase {

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oQueriesPack(), new N2oDataProvidersPack())
                .transformers(new TestEngineQueryTransformer());
    }

    @Test
    void test() {
        N2oQuery query = transform("net/n2oapp/framework/config/metadata/compile/query/testTestInvocationTransformer.query.xml")
                .get("testTestInvocationTransformer", N2oQuery.class);
        assertThat(query.getFields()[0].getId(), is("id"));
        assertThat(((QuerySimpleField) query.getFields()[0]).getSortingExpression(), is("id :idDirection"));
        assertThat(query.getFilters()[0].getFieldId(), is("id"));
        assertThat(query.getFilters()[0].getText(), is("id :eq :id"));
        assertThat(query.getFilters()[1].getText(), is("personName :like :personName"));
        assertThat(query.getFilters()[2].getFieldId(), is("test"));
        assertThat(query.getFilters()[2].getText(), is("test :eq :test_eq"));
    }
}
