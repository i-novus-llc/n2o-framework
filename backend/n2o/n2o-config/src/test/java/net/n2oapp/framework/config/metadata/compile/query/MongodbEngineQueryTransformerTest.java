package net.n2oapp.framework.config.metadata.compile.query;

import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.pipeline.ReadTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class MongodbEngineQueryTransformerTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/transformer/testMongodbQueryTransformer.query.xml"))
                .packs(new N2oAllDataPack(), new N2oAllPagesPack())
                .transformers(new MongodbEngineQueryTransformer());
    }

    @Test
    void testMongodbTransformer() {
        ReadTerminalPipeline pipeline =
                read("net/n2oapp/framework/config/metadata/transformer/testMongodbQueryTransformer.query.xml").transform();

        N2oQuery query = (N2oQuery) pipeline.get("testMongodbQueryTransformer", N2oQuery.class);
        assertThat(query.getFields()[0].getId(), is("id"));
        assertThat(((QuerySimpleField) query.getFields()[0]).getSelectExpression(), is("_id"));
        assertThat(query.getFields()[0].getMapping(), is("['_id'].toString()"));
        assertThat(((QuerySimpleField) query.getFields()[0]).getSortingExpression(), is("_id :idDirection"));
        assertThat(query.getFilters()[0].getFieldId(), is("id"));
        assertThat(query.getFilters()[0].getText(), is("{ _id: new ObjectId('#id') }"));

        assertThat(query.getFields()[1].getId(), is("name"));
        assertThat(((QuerySimpleField) query.getFields()[1]).getSelectExpression(), is("name"));
        assertThat(query.getFields()[1].getMapping(), nullValue());
        assertThat(((QuerySimpleField) query.getFields()[1]).getSortingExpression(), is("name :nameDirection"));
        assertThat(query.getFilters()[1].getFieldId(), is("name"));
        assertThat(query.getFilters()[1].getText(), is("{ 'name': '#name' }"));
        assertThat(query.getFilters()[2].getFieldId(), is("name"));
        assertThat(query.getFilters()[2].getText(), is("{ 'name': {$ne: '#notName' }}"));
        assertThat(query.getFilters()[3].getFieldId(), is("name"));
        assertThat(query.getFilters()[3].getText(), is("{ 'name': {$in: #userNameIn}}"));

        assertThat(((QuerySimpleField) query.getFields()[2]).getSortingExpression(), is("age :sortUserAge"));
        assertThat(((QuerySimpleField) query.getFields()[2]).getSortingMapping(), is("['sortUserAge']"));

    }
}
