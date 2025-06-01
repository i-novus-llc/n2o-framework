package net.n2oapp.framework.config.metadata.compile.query;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.query.QueryElementIOv5;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.object.N2oObjectCompiler;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;


class QueryFieldDefaultsCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oDataProvidersPack())
                .ios(new QueryElementIOv5())
                .compilers(new N2oQueryCompiler(), new N2oObjectCompiler())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/query_field_defaults/utQueryFieldDefaults.query.xml"));
    }

    @Test
    void defaultsBodyAndMapping() {
        CompiledQuery query = read().compile().get(new QueryContext("utQueryFieldDefaults"));
        QuerySimpleField field = query.getSimpleFieldsMap().get("gender.id");
        assertThat(field.getSelectExpression(), nullValue());
        assertThat(field.getMapping(), is("['gender.id']"));
        assertThat(field.getSortingExpression(), nullValue());
        assertThat(query.getFiltersMap().get("gender.id").get(FilterTypeEnum.EQ).getMapping(), is("['gender.id']"));
        assertThat(query.getFiltersMap().get("gender.id").get(FilterTypeEnum.EQ).getText(), nullValue());
    }

    @Test
    void defaultsBodyAndMappingWithExpression() {
        CompiledQuery query = read().compile().get(new QueryContext("utQueryFieldDefaults"));
        QuerySimpleField name = query.getSimpleFieldsMap().get("name");
        assertThat(name.getSelectExpression(), nullValue());
        assertThat(name.getMapping(), is("['name']"));
        assertThat(name.getSortingExpression(), nullValue());
        assertThat(name.getSortingMapping(), is("['nameDirection']"));
        N2oQuery.Filter filter = query.getFiltersMap().get("name").get(FilterTypeEnum.EQ);
        assertThat(filter.getText(), nullValue());
        assertThat(filter.getMapping(), is("['filter']"));
    }

}
