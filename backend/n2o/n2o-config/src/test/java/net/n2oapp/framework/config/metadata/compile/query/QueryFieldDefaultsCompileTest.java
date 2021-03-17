package net.n2oapp.framework.config.metadata.compile.query;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.object.ObjectElementIOv2;
import net.n2oapp.framework.config.io.query.QueryElementIOv4;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.object.N2oObjectCompiler;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;


public class QueryFieldDefaultsCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oDataProvidersPack())
                .ios(new QueryElementIOv4(), new ObjectElementIOv2())
                .compilers(new N2oQueryCompiler(), new N2oObjectCompiler())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/query_field_defaults/utQueryFieldDefaults.query.xml"));
    }

    @Test
    public void defaultsBodyAndMapping() {
        CompiledQuery query = read().compile().get(new QueryContext("utQueryFieldDefaults"));
        N2oQuery.Field field = query.getFieldsMap().get("gender.id");
        assertThat(field.getSelectBody(), nullValue());
        assertThat(field.getSelectMapping(), is("['gender.id']"));
        assertThat(field.getSortingBody(), nullValue());
        assertThat(query.getFiltersMap().get("gender.id").get(FilterType.eq).getMapping(), is("['gender.id']"));
        assertThat(query.getFiltersMap().get("gender.id").get(FilterType.eq).getText(), nullValue());
    }

    @Test
    public void defaultsBodyAndMappingWithExpression() {
        CompiledQuery query = read().compile().get(new QueryContext("utQueryFieldDefaults"));
        N2oQuery.Field name = query.getFieldsMap().get("name");
        assertThat(name.getSelectBody(), nullValue());
        assertThat(name.getSelectMapping(), is("['name']"));
        assertThat(name.getSortingBody(), nullValue());
        assertThat(name.getSortingMapping(), is("['nameDirection']"));
        N2oQuery.Filter filter = query.getFiltersMap().get("name").get(FilterType.eq);
        assertThat(filter.getText(), nullValue());
        assertThat(filter.getMapping(), is("['filter']"));
    }

}
