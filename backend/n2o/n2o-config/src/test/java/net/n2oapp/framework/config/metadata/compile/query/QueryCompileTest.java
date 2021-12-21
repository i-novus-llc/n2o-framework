package net.n2oapp.framework.config.metadata.compile.query;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.query.QueryElementIOv4;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueryCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/object/utAction.object.xml"))
                .ios(new QueryElementIOv4())
                .packs(new N2oPagesPack(), new N2oObjectsPack(), new N2oDataProvidersPack(), new N2oWidgetsPack(),
                        new N2oFieldSetsPack(), new N2oControlsPack())
                .compilers(new N2oQueryCompiler());
    }

    @Test
    public void testExpression() {
        CompiledQuery query = compile("net/n2oapp/framework/config/metadata/compile/query/utExpression.query.xml")
                .get(new QueryContext("utExpression"));
        assert query.getName().equals("utExpression");
        N2oQuery.Field manual = query.getFieldsMap().get("manual");
        assert "_test_".equals(manual.getSelectBody());
        assert "_test_".equals(manual.getSortingBody());
        assert "_test_".equals(manual.getFilterList()[0].getText());

        N2oQuery.Field auto = query.getFieldsMap().get("auto");
        assertThat(auto.getSelectMapping(), is("['auto']"));
        assertThat(auto.getSortingMapping(), is("['autoDirection']"));
        assertThat(auto.getFilterList()[0].getMapping(), is("['test']"));
        assertThat(auto.getFilterList()[1].getMapping(), is("['testLike']"));
        assertThat(auto.getFilterList()[2].getMapping(), is("['testIn']"));
        //todo тесты на генерацию тела не работают, т.к. компиляция этого еще не делает
//        assert "test".equals(auto.getSelectBody());
//        assert "test".equals(auto.getSortingBody());
//        assert query.getLists()[0].getInvocation() instanceof N2oSqlDataProvider;
//        assert ((N2oSqlDataProvider)query.getLists()[0].getInvocation()).getQuery().contains("test");
//        assert query.getCounts()[0].getInvocation() instanceof N2oSqlDataProvider;
//        assert ((N2oSqlDataProvider)query.getCounts()[0].getInvocation()).getQuery().contains("test");
//        assert query.getUniques() == null;

        N2oQuery.Field testFilter = query.getFieldsMap().get("testFilter");
        assertThat(testFilter.getFilterList()[0].getRequired(), is(true));
        assertThat(testFilter.getFilterList()[0].getFilterField(), is("testFilter_eq"));
        assertThat(testFilter.getFilterList()[1].getFilterField(), is("testFilter_in"));

        N2oQuery.Field withEmptySelect = query.getFieldsMap().get("withEmptySelect");
        assertThat(withEmptySelect.getSelectBody(), nullValue());
        assertThat(withEmptySelect.getSelectMapping(), is("['withEmptySelect']"));
        assertThat(withEmptySelect.getSortingMapping(), nullValue());
    }

//    @Test
//    public void testEmptyBody() {
//        CompiledQuery query = compile("net/n2oapp/framework/config/metadata/compile/query/testEmptyBody.query.xml")
//                .get(new QueryContext("testEmptyBody"));
//        assertThat(query.getFieldsMap().get("field").getSelectBody(), is(nullValue()));
//        assertThat(query.getFieldsMap().get("field").getSortingBody(), is(nullValue()));
//        assertThat(query.getFieldsMap().get("field").getFilterList()[0].getText(), is(nullValue()));
//    }

    @Test
    public void testFieldNames() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = compile(
                "net/n2oapp/framework/config/metadata/compile/query/utExpression.query.xml",
                "net/n2oapp/framework/config/metadata/compile/query/testQuery.page.xml");
        SimplePage page = (SimplePage) pipeline.get(new PageContext("testQuery"));
        Form form = (Form) page.getWidget();
        //label филда приходит из query
        assertThat(form.getComponent().getFieldsets()
                .get(0).getRows().get(0).getCols().get(0).getFields().get(0).getLabel(), is("f1"));

        //label филда приходит из query
        assertThat(form.getComponent().getFieldsets()
                .get(0).getRows().get(0).getCols().get(1).getFields().get(0).getLabel(), is("AUTO"));

        //label филда проставлен в виджете
        assertThat(form.getComponent().getFieldsets()
                .get(0).getRows().get(0).getCols().get(2).getFields().get(0).getLabel(), is("testManual"));

        //label филда проставлен в виджете
        assertThat(form.getComponent().getFieldsets()
                .get(0).getRows().get(0).getCols().get(3).getFields().get(0).getLabel(), is("f2"));
    }

    /**
     * Форма по своим полям собирает саб-модели и кладёт в QueryContext
     * проверяется, что по этому контексту получим Query с саб-моделями
     */
    @Test
    public void testSubModels() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = compile(
                "net/n2oapp/framework/config/metadata/compile/query/testSubModel.query.xml",
                "net/n2oapp/framework/config/metadata/compile/query/testSubModel.page.xml",
                "net/n2oapp/framework/config/metadata/compile/query/utExpression.query.xml");

        pipeline.get(new PageContext("testSubModel"));
       /*
        fixme списковые поля переезжают на datasource
        CompileContext compile = builder.route("/testSubModel/w", CompiledQuery.class, null);
        CompiledQuery query = pipeline.get((QueryContext) compile);
        assertThat(query.getSubModelQueries().size(), is(1));
        assertThat(query.getSubModelQueries().get(0).getSubModel(), is("field"));
        assertThat(query.getSubModelQueries().get(0).getQueryId(), is("testSubModel"));*/
    }


    @Test
    public void testRequiredPrefilters() {
        CompiledQuery query = compile("net/n2oapp/framework/config/metadata/compile/query/testRequiredFilters.query.xml")
                .get(new QueryContext("testRequiredFilters"));


        assertThat(query.getFiltersMap().get("test").get(FilterType.eq).getRequired(), is(true));
        assertThat(query.getValidations().get(0).getId(), is("test"));
        assertThat(query.getValidations().get(0).getFieldId(), is("test"));
        assertThat(query.getValidations().get(0).getSeverity(), is(SeverityType.danger));
        assertThat(query.getValidations().get(0).getMoment(), is(N2oValidation.ServerMoment.beforeQuery));
    }

    /**
     * Для тестового провайдера тело для <select/>, <sorting/>, <filter/> генерируется автоматически
     */
    @Test
    public void testTestDataProvider() {
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/query/testTestInvocationTransformer.query.xml"))
                .transformers(new TestEngineQueryTransformer());
        CompiledQuery query = builder.read().transform().compile().get(new QueryContext("testTestInvocationTransformer"));
        assertThat(query.getFieldsMap().get("id").getSelectBody(), is("id"));
        assertThat(query.getFieldsMap().get("id").getSortingBody(), is("id :idDirection"));
        assertThat(query.getFieldsMap().get("id").getFilterList()[0].getText(), is("id :eq :id"));
    }

    @Test
    public void testSeparatorsDefaultValue() {
        CompiledQuery query = compile("net/n2oapp/framework/config/metadata/compile/query/testSeparatorsDefaultValue" +
                ".query.xml")
                .get(new QueryContext("testSeparatorsDefaultValue"));
        N2oRestDataProvider list = (N2oRestDataProvider) query.getLists()[0].getInvocation();
        N2oRestDataProvider unique = (N2oRestDataProvider) query.getUniques()[0].getInvocation();
        N2oRestDataProvider count = (N2oRestDataProvider) query.getCounts()[0].getInvocation();

        assertThat(list.getFiltersSeparator(), is("&"));
        assertThat(list.getJoinSeparator(), is("&"));
        assertThat(list.getSelectSeparator(), is("&"));
        assertThat(list.getSortingSeparator(), is("&"));

        assertThat(unique.getFiltersSeparator(), is("separator"));
        assertThat(unique.getJoinSeparator(), is("separator"));
        assertThat(unique.getSelectSeparator(), is("separator"));
        assertThat(unique.getSortingSeparator(), is("separator"));

        assertThat(count.getFiltersSeparator(), is("&"));
        assertThat(count.getJoinSeparator(), is("&"));
        assertThat(count.getSelectSeparator(), is("&"));
        assertThat(count.getSortingSeparator(), is("&"));
    }
}
