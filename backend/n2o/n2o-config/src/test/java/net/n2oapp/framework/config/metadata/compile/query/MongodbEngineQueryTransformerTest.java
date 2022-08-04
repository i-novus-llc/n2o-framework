package net.n2oapp.framework.config.metadata.compile.query;

import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.SimpleField;
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
import static org.hamcrest.Matchers.nullValue;

public class MongodbEngineQueryTransformerTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
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
    public void testMomgodbTransformer() {
        ReadTerminalPipeline pipeline =
                read("net/n2oapp/framework/config/metadata/transformer/testMongodbQueryTransformer.query.xml").transform();

        N2oQuery query = (N2oQuery) pipeline.get("testMongodbQueryTransformer", N2oQuery.class);
        assertThat(query.getFields()[0].getId(), is("id"));
        assertThat(((SimpleField) query.getFields()[0]).getSelectExpression(), is("_id"));
        assertThat(query.getFields()[0].getMapping(), is("['_id'].toString()"));
        assertThat(((SimpleField) query.getFields()[0]).getSortingExpression(), is("_id :idDirection"));
        assertThat(query.getFilters()[0].getFieldId(), is("id"));
        assertThat(query.getFilters()[0].getText(), is("{ _id: new ObjectId('#id') }"));

        assertThat(query.getFields()[1].getId(), is("name"));
        assertThat(((SimpleField) query.getFields()[1]).getSelectExpression(), is("name"));
        assertThat(query.getFields()[1].getMapping(), nullValue());
        assertThat(((SimpleField) query.getFields()[1]).getSortingExpression(), is("name :nameDirection"));
        assertThat(query.getFilters()[1].getFieldId(), is("name"));
        assertThat(query.getFilters()[1].getText(), is("{ 'name': '#name' }"));
        assertThat(query.getFilters()[2].getFieldId(), is("name"));
        assertThat(query.getFilters()[2].getText(), is("{ 'name': {$ne: '#notName' }}"));
        assertThat(query.getFilters()[3].getFieldId(), is("name"));
        assertThat(query.getFilters()[3].getText(), is("{ 'name': {$in: #userNameIn}}"));

        assertThat(((SimpleField) query.getFields()[2]).getSortingExpression(), is("age :sortUserAge"));
        assertThat(((SimpleField) query.getFields()[2]).getSortingMapping(), is("['sortUserAge']"));

    }
}
