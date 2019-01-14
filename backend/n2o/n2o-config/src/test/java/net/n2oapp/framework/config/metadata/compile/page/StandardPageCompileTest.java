package net.n2oapp.framework.config.metadata.compile.page;


import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.region.LineRegion;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetDataProvider;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирвоание компиляции стандартной страницы
 */
public class StandardPageCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/object/utObjectField.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/page/utObjectField.page.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/object/utAction.object.xml"));

    }


    @Test
    public void layout() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPage.page.xml")
                .get(new PageContext("testStandardPage"));

        assertThat(page.getId(), is("testRoute"));
        assertThat(page.getObject().getId(), is("utObjectField"));
        assertThat(page.getLayout().getSrc(), is("SingleLayout"));
        assertThat(page.getLayout().getRegions().get("single").size(), is(3));
        assertThat(page.getLayout().getRegions().get("left").size(), is(1));
        assertThat(page.getLayout().getRegions().size(), is(2));
        assertThat(page.getLayout().getRegions().get("left").get(0).getSrc(), is("TabsRegion"));
        assertThat(page.getLayout().getRegions().get("single").get(0).getSrc(), is("ListRegion"));
        assertThat(page.getLayout().getRegions().get("single").get(1).getSrc(), is("PanelRegion"));
        assertThat(page.getLayout().getRegions().get("single").get(2).getSrc(), is("NoneRegion"));
        assertThat(page.getLayout().getRegions().get("single").get(0).getClass(), is(equalTo(LineRegion.class)));
        assertThat(page.getLayout().getRegions().get("single").get(0).getSrc(), is("ListRegion"));
        assertThat(page.getLayout().getRegions().get("single").get(0).getProperties().get("attr1"), is("testAttribute"));
        assertThat(page.getLayout().getRegions().get("single").get(0).getItems().get(0).getProperties().get("attr1"), is("htmlTestAttribute"));

        assertThat(page.getWidgets().size(), is(2));
        assertThat(page.getWidgets().get("testRoute_w0").getProperties().get("attr1"), is("htmlTestAttribute"));
        assertThat(page.getWidgets().get("testRoute_w0").getName(), is("test1"));
        assertThat(page.getWidgets().get("testRoute_w1").getName(), is("test2"));

        assertThat(page.getToolbar().get("tbTopLeft"), notNullValue());
        assertThat(page.getToolbar().get("tbTopLeft").get(0).getButtons().get(0).getActionId(), is("close"));
        assertThat(page.getToolbar().get("tbTopLeft").get(0).getButtons().get(1).getId(), is("subMenu1"));
        assertThat(page.getToolbar().get("tbTopLeft").get(0).getButtons().get(1).getSubMenu().get(0).getActionId(), is("test2"));
        assertThat(page.getToolbar().get("tbTopLeft").get(1).getButtons().get(0).getActionId(), is("test3"));
        assertThat(page.getToolbar().get("tbTopLeft").get(1).getButtons().get(1).getSubMenu().get(0).getActionId(), is("test4"));
        assertThat(page.getActions().containsKey("close"), is(true));

    }

    @Test
    public void routes() {
        PageContext context = new PageContext("testRoutes", "/page");
        Page page = compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/testRoutes.page.xml")
                .get(context);
        assertThat(page.getId(), is("page"));
        assertThat(page.getRoutes().getList().size(), is(11));
        assertThat(page.getRoutes().getList().get(0).getPath(), is("/page"));
        assertThat(page.getRoutes().getList().get(1).getPath(), is("/page/master"));
        assertThat(page.getRoutes().getList().get(2).getPath(), is("/page/master/:page_master_id"));
        assertThat(page.getRoutes().getList().get(3).getPath(), is("/page/master/:master_id/detail"));
        assertThat(page.getRoutes().getList().get(4).getPath(), is("/page/master/:master_id/detail/:page_detail_id"));
        assertThat(page.getRoutes().getList().get(5).getPath(), is("/page/master/:master_id/detail/:page_detail_id/detail4"));
        assertThat(page.getRoutes().getList().get(6).getPath(), is("/page/master/:master_id/detail/:page_detail_id/detail4/:page_detail4_id"));
        assertThat(page.getRoutes().getList().get(7).getPath(), is("/page/master/:master_id/detail/form"));
        assertThat(page.getRoutes().getList().get(8).getPath(), is("/page/master/:master_id/detail/form/:page_detail5_id"));//todo у формы нет selectedId
        assertThat(page.getRoutes().getList().get(9).getPath(), is("/page/master/:page_master_id/detail2"));
        assertThat(page.getRoutes().getList().get(10).getPath(), is("/page/master/:page_master_id/detail2/:page_detail2_id"));
        assertThat(page.getRoutes().getPathMapping().size(), is(6));
        assertThat(page.getRoutes().getPathMapping().get("page_master_id").getType(), is("n2o/widgets/CHANGE_SELECTED_ID"));
        assertThat(page.getRoutes().getPathMapping().get("page_master_id").getPayload().get("widgetId"), is("page_master"));
        assertThat(page.getRoutes().getPathMapping().get("page_master_id").getPayload().get("value"), is(":page_master_id"));
        assertThat(page.getRoutes().getPathMapping().get("page_detail_id").getType(), is("n2o/widgets/CHANGE_SELECTED_ID"));
        assertThat(page.getRoutes().getPathMapping().get("page_detail_id").getPayload().get("widgetId"), is("page_detail"));
        assertThat(page.getRoutes().getPathMapping().get("page_detail_id").getPayload().get("value"), is(":page_detail_id"));
        assertThat(page.getRoutes().getPathMapping().get("page_detail2_id").getType(), is("n2o/widgets/CHANGE_SELECTED_ID"));
        assertThat(page.getRoutes().getPathMapping().get("page_detail2_id").getPayload().get("widgetId"), is("page_detail2"));
        assertThat(page.getRoutes().getPathMapping().get("page_detail2_id").getPayload().get("value"), is(":page_detail2_id"));
        assertThat(page.getRoutes().getPathMapping().get("page_detail4_id").getType(), is("n2o/widgets/CHANGE_SELECTED_ID"));
        assertThat(page.getRoutes().getPathMapping().get("page_detail4_id").getPayload().get("widgetId"), is("page_detail4"));
        assertThat(page.getRoutes().getPathMapping().get("page_detail4_id").getPayload().get("value"), is(":page_detail4_id"));
        assertThat(page.getRoutes().getPathMapping().get("master_id").getPayload().get("widgetId"), is("page_master"));
        assertThat(page.getRoutes().getPathMapping().get("master_id").getPayload().get("value"), is(":master_id"));
        assertThat(page.getWidgets().get("page_detail").getFilter("parent.id").getParam(), is("master_id"));

        assertThat(((PageContext)route("/page/master/1").getContext(Page.class)).getClientPageId(), is(context.getClientPageId()));
        assertThat(((PageContext)route("/page/master/1/detail/2").getContext(Page.class)).getBreadcrumbs(), is(context.getBreadcrumbs()));
        assertThat(((PageContext)route("/page/master/1/detail/2/detail4").getContext(Page.class)).getClientPageId(), is(context.getClientPageId()));
        assertThat(((PageContext)route("/page/master/1/detail/2/detail4/3").getContext(Page.class)).getBreadcrumbs(), is(context.getBreadcrumbs()));

    }

    @Test
    public void masterDetails() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.page.xml")
                .get(new PageContext("testStandardPageDependency"));
        assertThat(page.getWidgets().size(), is(3));
        assertThat(page.getWidgets().get("testStandardPageDependency_master"), notNullValue());
        assertThat(page.getWidgets().get("testStandardPageDependency_detail"), notNullValue());
        assertThat(page.getWidgets().get("testStandardPageDependency_w0"), notNullValue());

        assertThat(page.getWidgets().get("testStandardPageDependency_detail").getDependency().getFetch().get(0).getOn(), is("models.resolve['testStandardPageDependency_master']"));
        assertThat(page.getWidgets().get("testStandardPageDependency_w0").getDependency().getFetch().get(0).getOn(), is("models.resolve['testStandardPageDependency_detail']"));

        List<Filter> preFilters = page.getWidgets().get("testStandardPageDependency_detail").getFilters();
        assertThat(preFilters.get(0).getFilterId(), is("parent.id"));
        assertThat(preFilters.get(0).getParam(), is("testStandardPageDependency_master_id"));
        assertThat(preFilters.get(0).getLink().getBindLink(), is("models.resolve['testStandardPageDependency_master'].id"));
        assertThat(preFilters.get(0).getLink().getValue(), nullValue());
        WidgetDataProvider dataProvider = page.getWidgets().get("testStandardPageDependency_detail").getDataProvider();
        assertThat(dataProvider.getPathMapping().get("testStandardPageDependency_master_id").getBindLink(), is("models.resolve['testStandardPageDependency_master'].id"));
        assertThat(((QueryContext)route("/testStandardPageDependency/master/:testStandardPageDependency_master_id/detail").getContext(CompiledQuery.class)).getFilters().size(), is(1));
        assertThat(((QueryContext)route("/testStandardPageDependency/master/:testStandardPageDependency_master_id/detail").getContext(CompiledQuery.class)).getFilters().get(0).getParam(), is("testStandardPageDependency_master_id"));

        preFilters = page.getWidgets().get("testStandardPageDependency_w0").getFilters();
        assertThat(preFilters.get(0).getFilterId(), is("parent.id"));
        assertThat(preFilters.get(0).getParam(), is("parent_id"));
        assertThat(preFilters.get(0).getLink().getBindLink(), is("models.resolve['testStandardPageDependency_detail'].parent.id"));
        assertThat(preFilters.get(0).getLink().getFieldId(), is("parent.id"));
        dataProvider = page.getWidgets().get("testStandardPageDependency_w0").getDataProvider();
        assertThat(dataProvider.getQueryMapping().get("parent_id").getBindLink(), is("models.resolve['testStandardPageDependency_detail'].parent.id"));

        assertThat(page.getRoutes().getQueryMapping().size(), is(6));
    }

    @Test
    public void preFilters() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/testWidgetPrefilters.page.xml")
                .get(new PageContext("testWidgetPrefilters"));
        assertThat(page.getRoutes().getQueryMapping().size(), is(10));

        WidgetDataProvider dataProvider = page.getWidgets().get("testWidgetPrefilters_detail1").getDataProvider();
        List<Filter> preFilters = page.getWidgets().get("testWidgetPrefilters_detail1").getFilters();
        assertThat(preFilters.get(0).getFilterId(), is("parent.id"));
        assertThat(preFilters.get(0).getParam(), is("testWidgetPrefilters_master1_id"));
        assertThat(preFilters.get(0).getLink().getBindLink(), is("models.resolve['testWidgetPrefilters_master1'].id"));
        assertThat(preFilters.get(0).getLink().getFieldId(), is("id"));
        assertThat(preFilters.get(1).getFilterId(), is("name"));
        assertThat(preFilters.get(1).getParam(), is("testWidgetPrefilters_detail1_name"));
        assertThat(preFilters.get(1).getLink().getBindLink(), nullValue());
        assertThat(preFilters.get(1).getLink().getValue(), is("test"));
        assertThat(preFilters.get(2).getFilterId(), is("genders*.id"));
        assertThat(preFilters.get(2).getParam(), is("testWidgetPrefilters_detail1_genders_id"));
        assertThat(preFilters.get(2).getLink().getValue(), is(Arrays.asList(1, 2)));
        assertThat(dataProvider.getPathMapping().get("testWidgetPrefilters_master1_id").getBindLink(), is("models.resolve['testWidgetPrefilters_master1'].id"));
        assertThat(dataProvider.getQueryMapping().get("testWidgetPrefilters_detail1_name").getBindLink(), nullValue());
        assertThat(dataProvider.getQueryMapping().get("testWidgetPrefilters_detail1_name").getValue(), is("test"));
        assertThat(dataProvider.getQueryMapping().get("testWidgetPrefilters_detail1_genders_id").getBindLink(), nullValue());
        assertThat(dataProvider.getQueryMapping().get("testWidgetPrefilters_detail1_genders_id").getValue(), is(Arrays.asList(1, 2)));
        QueryContext queryCompileContext = (QueryContext) route("/testWidgetPrefilters/master1/:testWidgetPrefilters_master1_id/detail1").getContext(CompiledQuery.class);
        assertThat(queryCompileContext.getFilters().size(), is(3));
        assertThat(queryCompileContext.getFilters().get(0).getParam(), is("testWidgetPrefilters_master1_id"));
        assertThat(queryCompileContext.getFilters().get(1).getParam(), is("testWidgetPrefilters_detail1_name"));
        assertThat(queryCompileContext.getFilters().get(2).getParam(), is("testWidgetPrefilters_detail1_genders_id"));

        dataProvider = page.getWidgets().get("testWidgetPrefilters_detail2").getDataProvider();
        preFilters = page.getWidgets().get("testWidgetPrefilters_detail2").getFilters();
        assertThat(preFilters.get(0).getFilterId(), is("name"));
        assertThat(preFilters.get(0).getParam(), is("testWidgetPrefilters_detail2_name"));
        assertThat(preFilters.get(0).getLink().getBindLink(), is("models.filter['testWidgetPrefilters_master2']"));
        assertThat(preFilters.get(0).getLink().getValue(), is("`name`"));
        assertThat(preFilters.get(1).getFilterId(), is("genders*.id"));
        assertThat(preFilters.get(1).getParam(), is("testWidgetPrefilters_detail2_genders_id"));
        assertThat(preFilters.get(1).getLink().getBindLink(), is("models.filter['testWidgetPrefilters_master2']"));
        assertThat(preFilters.get(1).getLink().getValue(), is("`gender.id`"));
        assertThat(dataProvider.getQueryMapping().get("testWidgetPrefilters_detail2_name").getBindLink(), is("models.filter['testWidgetPrefilters_master2']"));
        assertThat(dataProvider.getQueryMapping().get("testWidgetPrefilters_detail2_name").getValue(), is("`name`"));
        assertThat(dataProvider.getQueryMapping().get("testWidgetPrefilters_detail2_genders_id").getBindLink(), is("models.filter['testWidgetPrefilters_master2']"));
        assertThat(dataProvider.getQueryMapping().get("testWidgetPrefilters_detail2_genders_id").getValue(), is("`gender.id`"));
        queryCompileContext = (QueryContext) route("/testWidgetPrefilters/detail2").getContext(CompiledQuery.class);
        assertThat(queryCompileContext.getFilters().size(), is(2));
        assertThat(queryCompileContext.getFilters().get(0).getParam(), is("testWidgetPrefilters_detail2_name"));
        assertThat(queryCompileContext.getFilters().get(1).getParam(), is("testWidgetPrefilters_detail2_genders_id"));

        preFilters = page.getWidgets().get("testWidgetPrefilters_detail3").getFilters();
        assertThat(preFilters.get(0).getFilterId(), is("genders*.id"));
        assertThat(preFilters.get(0).getParam(), is("testWidgetPrefilters_detail3_genders_id"));
        assertThat(preFilters.get(0).getLink().getBindLink(), is("models.filter['testWidgetPrefilters_master2']"));
        assertThat(preFilters.get(0).getLink().getValue(), is("`gender.map(function(t){return t.id})`"));
    }
}