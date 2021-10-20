package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.action.SelectedWidgetPayload;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.LineRegion;
import net.n2oapp.framework.api.metadata.meta.region.PanelRegion;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции стандартной страницы
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
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/page/utObjectField.page.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/object/utAction.object.xml"));
    }


    @Test
    public void layout() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPage.page.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml")
                .get(new PageContext("testStandardPage"));

        assertThat(page.getId(), is("testRoute"));
        assertThat(page.getObject().getId(), is("utBlank"));

        assertThat(page.getSrc(), is("StandardPage"));
        assertThat(page.getRegions().get("single").size(), is(3));
        assertThat(page.getRegions().get("left").size(), is(1));
        assertThat(page.getRegions().size(), is(2));
        assertThat(page.getRegions().get("left").get(0).getSrc(), is("TabsRegion"));
        assertThat(page.getRegions().get("single").get(0).getSrc(), is("ListRegion"));
        assertThat(page.getRegions().get("single").get(1).getSrc(), is("PanelRegion"));
        assertThat(((PanelRegion) page.getRegions().get("single").get(1)).getStyle().get("width"), is("300px"));
        assertThat(((PanelRegion) page.getRegions().get("single").get(1)).getStyle().get("marginLeft"), is("10px"));
        assertThat(page.getRegions().get("single").get(2).getSrc(), is("NoneRegion"));
        assertThat(page.getRegions().get("single").get(0).getClass(), is(equalTo(LineRegion.class)));
        assertThat(page.getRegions().get("single").get(0).getSrc(), is("ListRegion"));
        assertThat(page.getRegions().get("single").get(0).getProperties().get("attr1"), is("testAttribute"));
        assertThat(((Widget) page.getRegions().get("single").get(0).getContent().get(0))
                .getProperties().get("attr1"), is("htmlTestAttribute"));

        List<Compiled> widgets = page.getRegions().get("single").get(0).getContent();
        assertThat(widgets.size(), is(2));
        assertThat(((Widget) widgets.get(0)).getProperties().get("attr1"), is("htmlTestAttribute"));
        assertThat(((Widget) widgets.get(0)).getName(), is("test1"));
        assertThat(((Widget) widgets.get(1)).getName(), is("test2"));

        assertThat(page.getToolbar().get("TopLeft"), notNullValue());
        assertThat(page.getToolbar().get("TopLeft").get(0).getButtons().get(1).getId(), is("subMenu1"));
        assertThat(page.getToolbar().getButton("close"), notNullValue());

    }

    @Test
    public void routes() {
        PageContext context = new PageContext("testRoutes", "/page");
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.query.xml",
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
        assertThat(((SelectedWidgetPayload) page.getRoutes().getPathMapping().get("page_master_id").getPayload()).getWidgetId(), is("page_master"));
        assertThat(((SelectedWidgetPayload) page.getRoutes().getPathMapping().get("page_master_id").getPayload()).getValue(), is(":page_master_id"));
        assertThat(page.getRoutes().getPathMapping().get("page_detail_id").getType(), is("n2o/widgets/CHANGE_SELECTED_ID"));
        assertThat(((SelectedWidgetPayload) page.getRoutes().getPathMapping().get("page_detail_id").getPayload()).getWidgetId(), is("page_detail"));
        assertThat(((SelectedWidgetPayload) page.getRoutes().getPathMapping().get("page_detail_id").getPayload()).getValue(), is(":page_detail_id"));
        assertThat(page.getRoutes().getPathMapping().get("page_detail2_id").getType(), is("n2o/widgets/CHANGE_SELECTED_ID"));
        assertThat(((SelectedWidgetPayload) page.getRoutes().getPathMapping().get("page_detail2_id").getPayload()).getWidgetId(), is("page_detail2"));
        assertThat(((SelectedWidgetPayload) page.getRoutes().getPathMapping().get("page_detail2_id").getPayload()).getValue(), is(":page_detail2_id"));
        assertThat(page.getRoutes().getPathMapping().get("page_detail4_id").getType(), is("n2o/widgets/CHANGE_SELECTED_ID"));
        assertThat(((SelectedWidgetPayload) page.getRoutes().getPathMapping().get("page_detail4_id").getPayload()).getWidgetId(), is("page_detail4"));
        assertThat(((SelectedWidgetPayload) page.getRoutes().getPathMapping().get("page_detail4_id").getPayload()).getValue(), is(":page_detail4_id"));
        assertThat(((Widget) page.getRegions().get("test").get(0).getContent().get(1)).getFilter("parent.id").getParam(), is("master_id"));

        assertThat(((PageContext) route("/page/master/1", Page.class)).getClientPageId(), is(context.getClientPageId()));
        assertThat(((PageContext) route("/page/master/1/detail/2", Page.class)).getBreadcrumbs(), is(context.getBreadcrumbs()));
        assertThat(((PageContext) route("/page/master/1/detail/2/detail4", Page.class)).getClientPageId(), is(context.getClientPageId()));
        assertThat(((PageContext) route("/page/master/1/detail/2/detail4/3", Page.class)).getBreadcrumbs(), is(context.getBreadcrumbs()));
    }

    @Test
    public void masterDetails() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.object.xml",
                "net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.page.xml")
                .get(new PageContext("testStandardPageDependency"));
        Table master = (Table) page.getRegions().get("left").get(0).getContent().get(0);
        Table detail = (Table) page.getRegions().get("right").get(0).getContent().get(0);
        Table panel = (Table) page.getRegions().get("right").get(0).getContent().get(1);

        assertThat(master, notNullValue());
        assertThat(detail, notNullValue());
        assertThat(panel, notNullValue());

        assertThat(detail.getDependency().getFetch().get(0).getOn(), is("models.resolve['testStandardPageDependency_master']"));
        assertThat(panel.getDependency().getFetch().get(0).getOn(), is("models.resolve['testStandardPageDependency_detail']"));

        List<Filter> preFilters = detail.getFilters();
        assertThat(preFilters.get(0).getFilterId(), is("parent.id"));
        assertThat(preFilters.get(0).getParam(), is("testStandardPageDependency_master_id"));
        assertThat(preFilters.get(0).getLink().getBindLink(), is("models.resolve['testStandardPageDependency_master'].id"));
        assertThat(preFilters.get(0).getLink().getValue(), nullValue());
        ClientDataProvider dataProvider = detail.getDataProvider();
        assertThat(dataProvider.getPathMapping().get("testStandardPageDependency_master_id").getBindLink(), is("models.resolve['testStandardPageDependency_master'].id"));
        assertThat(((QueryContext) route("/testStandardPageDependency/master/:testStandardPageDependency_master_id/detail", CompiledQuery.class))
                .getFilters().size(), is(1));
        assertThat(((QueryContext) route("/testStandardPageDependency/master/:testStandardPageDependency_master_id/detail", CompiledQuery.class))
                .getFilters().get(0).getParam(), is("testStandardPageDependency_master_id"));

        preFilters = panel.getFilters();
        assertThat(preFilters.get(0).getFilterId(), is("parent.id"));
        assertThat(preFilters.get(0).getParam(), is("parent_id"));
        assertThat(preFilters.get(0).getLink().getBindLink(), is("models.resolve['testStandardPageDependency_detail'].parent.id"));
        assertThat(preFilters.get(0).getLink().getFieldId(), is("parent.id"));
        dataProvider = panel.getDataProvider();
        assertThat(dataProvider.getQueryMapping().get("parent_id").getBindLink(),
                is("models.resolve['testStandardPageDependency_detail'].parent.id"));

        assertThat(page.getRoutes().getQueryMapping().size(), is(8));

        //Условия видимости виджетов
        assertThat(panel.getVisible(), is(true));
        assertThat(detail.getDependency().getVisible().get(0).getOn(), is("models.resolve['testStandardPageDependency_master']"));
        assertThat(detail.getDependency().getVisible().get(0).getCondition(), is("parent.id == 1"));

        //проверим что у кнопки delete родительский pathmapping скопировался
        assertThat(((InvokeAction) panel.getToolbar().getButton("delete").getAction()).getPayload().getDataProvider().getPathMapping()
                        .get("testStandardPageDependency_master_id").getBindLink(),
                is("models.resolve['testStandardPageDependency_master'].id"));

    }

    @Test
    public void preFilters() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/testWidgetPrefilters.page.xml")
                .get(new PageContext("testWidgetPrefilters"));

        Table detail = (Table) page.getRegions().get("left").get(0).getContent().get(1);
        ClientDataProvider dataProvider = detail.getDataProvider();
        List<Filter> preFilters = detail.getFilters();
        assertThat(preFilters.get(0).getFilterId(), is("parent.id"));
        assertThat(preFilters.get(0).getParam(), is("testWidgetPrefilters_master1_id"));
        assertThat(preFilters.get(0).getLink().getBindLink(), is("models.resolve['testWidgetPrefilters_master1'].id"));
        assertThat(preFilters.get(0).getLink().getFieldId(), is("id"));
        assertThat(preFilters.get(1).getFilterId(), is("name"));
        assertThat(preFilters.get(1).getParam(), is("nameParam"));
        assertThat(preFilters.get(1).getLink().getBindLink(), nullValue());
        assertThat(preFilters.get(1).getLink().getValue(), is("test"));
        assertThat(preFilters.get(2).getFilterId(), is("genders*.id"));
        assertThat(preFilters.get(2).getParam(), is("testWidgetPrefilters_detail1_genders_id"));
        assertThat(preFilters.get(2).getLink().getValue(), is(Arrays.asList(1, 2)));
        assertThat(dataProvider.getPathMapping().get("testWidgetPrefilters_master1_id").getBindLink(),
                is("models.resolve['testWidgetPrefilters_master1'].id"));
        assertThat(dataProvider.getQueryMapping().get("nameParam").getBindLink(), nullValue());
        assertThat(dataProvider.getQueryMapping().get("nameParam").getValue(), is("test"));
        assertThat(dataProvider.getQueryMapping().get("testWidgetPrefilters_detail1_genders_id").getBindLink(), nullValue());
        assertThat(dataProvider.getQueryMapping().get("testWidgetPrefilters_detail1_genders_id").getValue(), is(Arrays.asList(1, 2)));
        QueryContext queryCompileContext = (QueryContext) route("/testWidgetPrefilters/master1/:testWidgetPrefilters_master1_id/detail1", CompiledQuery.class);
        assertThat(queryCompileContext.getFilters().size(), is(3));
        assertThat(queryCompileContext.getFilters().get(0).getParam(), is("testWidgetPrefilters_master1_id"));
        assertThat(queryCompileContext.getFilters().get(1).getParam(), is("nameParam"));
        assertThat(queryCompileContext.getFilters().get(2).getParam(), is("testWidgetPrefilters_detail1_genders_id"));

        Table detail2 = (Table) page.getRegions().get("right").get(0).getContent().get(1);
        dataProvider = detail2.getDataProvider();
        preFilters = detail2.getFilters();
        assertThat(preFilters.get(0).getFilterId(), is("name"));
        assertThat(preFilters.get(0).getParam(), is("testWidgetPrefilters_detail2_name"));
        assertThat(preFilters.get(0).getLink().getBindLink(), is("models.filter['testWidgetPrefilters_master2']"));
        assertThat(preFilters.get(0).getLink().getValue(), is("`name`"));
        assertThat(preFilters.get(1).getFilterId(), is("genders*.id"));
        assertThat(preFilters.get(1).getParam(), is("testWidgetPrefilters_detail2_genders_id"));
        assertThat(preFilters.get(1).getLink().getBindLink(), is("models.filter['testWidgetPrefilters_master2']"));
        assertThat(preFilters.get(1).getLink().getValue(), is("`gender.id`"));
        assertThat(dataProvider.getQueryMapping().get("testWidgetPrefilters_detail2_name").getBindLink(),
                is("models.filter['testWidgetPrefilters_master2']"));
        assertThat(dataProvider.getQueryMapping().get("testWidgetPrefilters_detail2_name").getValue(),
                is("`name`"));
        assertThat(dataProvider.getQueryMapping().get("testWidgetPrefilters_detail2_genders_id").getBindLink(),
                is("models.filter['testWidgetPrefilters_master2']"));
        assertThat(dataProvider.getQueryMapping().get("testWidgetPrefilters_detail2_genders_id").getValue(),
                is("`gender.id`"));
        queryCompileContext = (QueryContext) route("/testWidgetPrefilters/detail2", CompiledQuery.class);
        assertThat(queryCompileContext.getFilters().size(), is(2));
        assertThat(queryCompileContext.getFilters().get(0).getParam(), is("testWidgetPrefilters_detail2_name"));
        assertThat(queryCompileContext.getFilters().get(1).getParam(), is("testWidgetPrefilters_detail2_genders_id"));

        preFilters = ((Table) page.getRegions().get("right").get(0).getContent().get(2)).getFilters();
        assertThat(preFilters.get(0).getFilterId(), is("genders*.id"));
        assertThat(preFilters.get(0).getParam(), is("testWidgetPrefilters_detail3_genders_id"));
        assertThat(preFilters.get(0).getLink().getBindLink(), is("models.filter['testWidgetPrefilters_master2']"));
        assertThat(preFilters.get(0).getLink().getValue(), is("`gender.map(function(t){return t.id})`"));
    }


    /**
     * Если в роуте виджета есть параметры от предыдущих виджетов,
     * то этот виджет должен иметь ссылки на эти параметры
     */
    @Test
    public void testChainFetching() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.query.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testChainWidgetFetching.page.xml")
                .get(new PageContext("testChainWidgetFetching"));

        Form form = (Form) page.getRegions().get("right").get(0).getContent().get(0);
        Form form2 = (Form) page.getRegions().get("right").get(1).getContent().get(0);
        Form form3 = (Form) page.getRegions().get("right").get(2).getContent().get(0);

        assertThat(form.getDataProvider().getPathMapping().size(), is(1));
        assertThat(form.getDataProvider().getPathMapping().get("param1").getBindLink(), is("models.resolve['table'].id"));

        assertThat(form2.getDataProvider().getPathMapping().size(), is(2));
        assertThat(form2.getDataProvider().getPathMapping().get("param1").getBindLink(), is("models.resolve['table'].id"));
        assertThat(form2.getDataProvider().getPathMapping().get("param2").getBindLink(), is("models.resolve['form'].id"));

        assertThat(form3.getDataProvider().getPathMapping().size(), is(3));
        assertThat(form3.getDataProvider().getPathMapping().get("param1").getBindLink(), is("models.resolve['table'].id"));
        assertThat(form3.getDataProvider().getPathMapping().get("param2").getBindLink(), is("models.resolve['form'].id"));
        assertThat(form3.getDataProvider().getPathMapping().get("param3").getBindLink(), is("models.resolve['form2'].id"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateObjectIdForMainWidget() {
        PageContext validateObjectIdForMainWidget = new PageContext("testStandardPageObject");
        validateObjectIdForMainWidget.setSubmitOperationId("test");
        compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageObject.page.xml")
                .get(validateObjectIdForMainWidget);

    }

    @Test
    public void testPageTitle() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageTitle.page.xml")
                .get(new PageContext("testStandardPageTitle"));
        assertThat(page.getPageProperty().getTitle(), is("title"));
        assertThat(page.getPageProperty().getHtmlTitle(), is("tab title"));
    }

    @Test
    public void testPageTitleInModal() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageModalTitle.page.xml")
                .get(new ModalPageContext("testStandardPageModalTitle", "/modal"));
        assertThat(page.getPageProperty().getTitle(), nullValue());
        assertThat(page.getPageProperty().getModalHeaderTitle(), is("testPage"));
    }
}