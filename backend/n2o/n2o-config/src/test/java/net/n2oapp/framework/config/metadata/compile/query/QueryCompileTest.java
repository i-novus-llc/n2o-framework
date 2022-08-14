package net.n2oapp.framework.config.metadata.compile.query;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
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
    public void testEmptyBody() {
        CompiledQuery query = compile("net/n2oapp/framework/config/metadata/compile/query/testEmptyBody.query.xml")
                .get(new QueryContext("testEmptyBody"));
        assertThat(query.getSimpleFieldsMap().get("field").getSelectExpression(), is(nullValue()));
        assertThat(query.getSimpleFieldsMap().get("field").getSortingExpression(), is(nullValue()));
    }

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
        CompileContext compile = builder.route("/testSubModel/w", CompiledQuery.class, null);
        CompiledQuery query = pipeline.get((QueryContext) compile);
        assertThat(query.getSubModelQueries().size(), is(1));
        assertThat(query.getSubModelQueries().get(0).getSubModel(), is("field"));
        assertThat(query.getSubModelQueries().get(0).getQueryId(), is("testSubModel"));
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
        assertThat(query.getSimpleFieldsMap().get("id").getSelectExpression(), is("id"));
        assertThat(query.getSimpleFieldsMap().get("id").getSortingExpression(), is("id :idDirection"));
        assertThat(query.getFilterFieldsMap().get("id").getText(), is("id :eq :id"));
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
