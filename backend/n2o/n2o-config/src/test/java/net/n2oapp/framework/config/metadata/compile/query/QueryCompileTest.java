package net.n2oapp.framework.config.metadata.compile.query;

import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.object.ObjectElementIOv1;
import net.n2oapp.framework.config.io.query.QueryElementIOv4;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.compile.object.N2oObjectCompiler;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
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
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/object/utObjectField.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/object/utAction.object.xml"))
                .ios(new ObjectElementIOv1(), new QueryElementIOv4())
                .packs(new N2oDataProvidersPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oInvocationV2ReadersPack())
                .compilers(new N2oQueryCompiler(), new N2oObjectCompiler());
    }

    @Test
    public void testExpression() {
        CompiledQuery query = compile("net/n2oapp/framework/config/metadata/compile/query/utExpression.query.xml")
                .get(new QueryContext("utExpression"));
        assert query.getName().equals("utExpression");
        assert query.getObject().getId().equals("utObjectField");
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
        assertThat(testFilter.getFilterList()[0].getFilterField(), is("testFilter_eq"));
        assertThat(testFilter.getFilterList()[1].getFilterField(), is("testFilter_in"));
    }

    @Test
    public void testFieldNames() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = compile(
                "net/n2oapp/framework/config/metadata/compile/query/utExpression.query.xml",
                "net/n2oapp/framework/config/metadata/compile/query/testQuery.widget.xml");
        Form form = (Form) pipeline.get(new WidgetContext("testQuery"));

        //label филда приходит из объекта
        assertThat(((StandardField) form.getComponent().getFieldsets()
                .get(0).getRows().get(0).getCols().get(0).getFields().get(0)).getLabel(), is("F1"));

        //label филда приходит из query
        assertThat(((StandardField) form.getComponent().getFieldsets()
                .get(0).getRows().get(0).getCols().get(1).getFields().get(0)).getLabel(), is("AUTO"));

        //label филда проставлен в виджете
        assertThat(((StandardField) form.getComponent().getFieldsets()
                .get(0).getRows().get(0).getCols().get(2).getFields().get(0)).getLabel(), is("testManual"));

        //label филда проставлен в объекте, самого филда нет в query
        assertThat(((StandardField) form.getComponent().getFieldsets()
                .get(0).getRows().get(0).getCols().get(3).getFields().get(0)).getLabel(), is("F2"));
    }

    /**
     * Форма по своим полям собирает саб-модели и кладёт в QueryContext
     * проверяется, что по этому контексту получим Query с саб-моделями
     */
    @Test
    public void testSubModels() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = compile(
                "net/n2oapp/framework/config/metadata/compile/query/testSubModel.query.xml",
                "net/n2oapp/framework/config/metadata/compile/query/testSubModel.widget.xml",
                "net/n2oapp/framework/config/metadata/compile/query/utExpression.query.xml");

        pipeline.get(new WidgetContext("testSubModel"));

        builder.getEnvironment().getRouteRegister().forEach(
                routeInfo -> {
                    if (routeInfo.getUrlPattern().equals("/w")) {
                        CompiledQuery query = pipeline.get((QueryContext) routeInfo.getContext());
                        assertThat(query.getSubModelQueries().size(), is(1));
                        assertThat(query.getSubModelQueries().get(0).getSubModel(), is("field"));
                        assertThat(query.getSubModelQueries().get(0).getQueryId(), is("testSubModel"));
                    }
                }
        );

    }
}
