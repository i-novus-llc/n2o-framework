package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.ReduxAction;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.UpdateModelPayload;
import net.n2oapp.framework.api.metadata.meta.action.clear.ClearAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshAction;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshPayload;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.control.InputSelect;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.CloseActionElementIOV1;
import net.n2oapp.framework.config.io.action.InvokeActionElementIOV1;
import net.n2oapp.framework.config.io.action.OpenPageElementIOV1;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Проверка компиляции open-page
 */
class OpenPageCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oActionsPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oControlsPack(), new N2oAllDataPack(), new N2oCellsPack(), new N2oFieldSetsPack());
        builder.ios(new OpenPageElementIOV1(), new InvokeActionElementIOV1(), new CloseActionElementIOV1());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageDynamicPage.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testGender.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage2.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage4.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction1.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction2.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageMasterDetail.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testDefaultValue.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testPreFilter.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageWithRefWidget.widget.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testRefbook.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testCustomBreadcrumb.page.xml"));
    }

    @Test
    void filterModel() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .get(new PageContext("testOpenPageSimplePage"));
        PageContext context = (PageContext) route("/page/action1", Page.class);
        SimplePage openPage = (SimplePage) read().compile().get(context);

        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        InvokeAction submit = (InvokeAction) ((MultiAction) openPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(0);
        InvokeActionPayload submitPayload = submit.getPayload();
        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/page/action1/multi1"));
        assertThat(submitPayload.getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(submitPayload.getModel(), is(ReduxModel.resolve));
        assertThat(submitPayload.getDatasource(), is("page_action1_w1"));
        RefreshAction refresh = (RefreshAction) ((MultiAction) openPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(1);
        assertThat(((RefreshPayload) refresh.getPayload()).getDatasource(), is("page_test"));
        LinkAction redirect = (LinkAction) ((MultiAction) openPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(2);
        assertThat(redirect.getUrl(), is("/page"));
        ActionContext submitContext = (ActionContext) route("/page/action1/multi1", CompiledObject.class);
        assertThat(submitContext.getRedirect(), nullValue());

        LinkActionImpl close = (LinkActionImpl) openPage.getToolbar().getButton("close").getAction();
        assertThat(close.getUrl(), is("/page"));
        assertThat(close.getTarget(), is(Target.application));
    }

    @Test
    void resolveModel() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml")
                .get(new PageContext("testOpenPageSimplePage", "/page"));
        LinkActionImpl action = (LinkActionImpl) page.getWidget().getToolbar().getButton("id2").getAction();
        assertThat(action.getPathMapping().get("page_test_id").getBindLink(), is("models.resolve['page_test']"));
        assertThat(action.getPathMapping().get("page_test_id").getValue(), is("`id`"));
        assertThat(action.getQueryMapping().size(), is(0));

        PageContext context = (PageContext) route("/page/123/action2", Page.class);
        assertThat(context.getPreFilters().size(), is(1));
        assertThat(context.getPreFilters().get(0).getRefPageId(), is("page"));
        assertThat(context.getPreFilters().get(0).getDatasourceId(), is("test"));
        assertThat(context.getPreFilters().get(0).getModel(), is(ReduxModel.resolve));
        assertThat(context.getPreFilters().get(0).getParam(), is("page_test_id"));
        assertThat(context.getPreFilters().get(0).getType(), is(FilterType.eq));
        assertThat(context.getParentModelLinks().get(1).getFieldId(), is("id"));
        assertThat(context.getParentModelLinks().get(1).getDatasource(), is("page_test"));
        assertThat(context.getParentModelLinks().get(1).getSubModelQuery(), notNullValue());
        assertThat(context.getParentModelLinks().get(1).getSubModelQuery().getQueryId(), is("testShowModal"));
        assertThat(context.getParentModelLinks().get(0).getFieldId(), is("id"));
        assertThat(context.getParentModelLinks().get(0).getDatasource(), is("page_test"));
        assertThat(context.getParentModelLinks().get(0).getSubModelQuery(), nullValue());

        SimplePage openPage = (SimplePage) read().compile().get(context);
        assertThat(openPage.getId(), is("page_action2"));
        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        ClientDataProvider provider = ((StandardDatasource) openPage.getDatasources().get(openPage.getWidget().getDatasource())).getProvider();
        assertThat(provider.getPathMapping().get("page_test_id").getBindLink(), is("models.resolve['page_test']"));
        assertThat(provider.getPathMapping().get("page_test_id").getValue(), is("`id`"));

        InvokeAction submit = (InvokeAction) ((MultiAction) openPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(0);
        InvokeActionPayload submitPayload = submit.getPayload();
        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/page/:page_test_id/action2/multi1"));
        assertThat(submitPayload.getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(submitPayload.getModel(), is(ReduxModel.resolve));
        assertThat(submitPayload.getDatasource(), is("page_action2_main"));
        RefreshAction refresh = (RefreshAction) ((MultiAction) openPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(1);
        assertThat(((RefreshPayload) refresh.getPayload()).getDatasource(), is("page_test"));
        LinkAction redirect = (LinkAction) ((MultiAction) openPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(2);
        assertThat(redirect.getUrl(), is("/page"));

        ActionContext submitContext = (ActionContext) route("/page/123/action2/multi1", CompiledObject.class);
        assertThat(submitContext.getRedirect(), nullValue());

        LinkActionImpl close = (LinkActionImpl) openPage.getToolbar().getButton("close").getAction();
        assertThat(close.getUrl(), is("/page"));
        assertThat(close.getTarget(), is(Target.application));
    }

    @Test
    void breadcrumb() {
        // проверка breadcrumb при открытии open-page из таблицы
        DataSet data = new DataSet().add("parent_id", 123);
        PageContext context = new PageContext("testOpenPageSimplePage", "/page/:parent_id/view");
        context.setBreadcrumbs(Collections.singletonList(new Breadcrumb("parent", "/page/:parent_id")));
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .bind().get(context, data);
        assertThat(page.getRoutes().getSet().contains("/page/:parent_id/view"), is(true));

        Page createPage = routeAndGet("/page/123/view/action1", Page.class);
        assertThat(createPage.getRoutes().getSet().contains("/page/:parent_id/view/action1"), is(true));
        assertThat(createPage.getBreadcrumb().size(), is(3));
        assertThat(createPage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(createPage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(createPage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(createPage.getBreadcrumb().get(1).getPath(), is("/page/123/view"));
        assertThat(createPage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(createPage.getBreadcrumb().get(2).getPath(), nullValue());

        Page updatePage = routeAndGet("/page/123/view/456/action2", Page.class);
        assertThat(updatePage.getRoutes().getSet().contains("/page/:parent_id/view/:page_test_id/action2"), is(true));
        assertThat(updatePage.getBreadcrumb().size(), is(3));
        assertThat(updatePage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(updatePage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(updatePage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(updatePage.getBreadcrumb().get(1).getPath(), is("/page/123/view"));
        assertThat(updatePage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(updatePage.getBreadcrumb().get(2).getPath(), nullValue());

        HashMap<String, String[]> params = new HashMap<>();
        data.put("name", "ivan");
        data.put("secondName", "ivanov");
        Page masterDetailPage = routeAndGet("/page/123/view/456/masterDetail", Page.class, params);
        assertThat(masterDetailPage.getRoutes().getSet().contains("/page/:parent_id/view/:page_test_id/masterDetail"), is(true));
        assertThat(masterDetailPage.getBreadcrumb().size(), is(3));
        assertThat(masterDetailPage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(masterDetailPage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(masterDetailPage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(masterDetailPage.getBreadcrumb().get(1).getPath(), is("/page/123/view"));
        assertThat(masterDetailPage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(masterDetailPage.getBreadcrumb().get(2).getPath(), nullValue());

        Page level3Page = routeAndGet("/page/123/view/456/masterDetail/level3", Page.class, params);
        assertThat(level3Page.getBreadcrumb().size(), is(4));
        assertThat(level3Page.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(level3Page.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(level3Page.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(level3Page.getBreadcrumb().get(1).getPath(), is("/page/123/view"));
        assertThat(level3Page.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(level3Page.getBreadcrumb().get(2).getPath(), is("/page/123/view/456/masterDetail?name=:name&surname=:surname&secondName=test"));

        // проверка breadcrumb при открытии open-page не из таблицы, а другого виджета
        // при этом в parentId не должны сохраняться id выбранных записей
        context = new PageContext("testOpenPageSimplePage2", "/page/:parent_id/view2");
        context.setBreadcrumbs(Collections.singletonList(new Breadcrumb("parent", "/page/:parent_id")));
        Page page2 = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage2.page.xml")
                .bind().get(context, data);
        assertThat(page2.getRoutes().getSet().contains("/page/:parent_id/view2"), is(true));
        updatePage = routeAndGet("/page/123/view2/456/action2", Page.class);
        assertThat(updatePage.getRoutes().getSet().contains("/page/:parent_id/view2/:page_test_id/action2"), is(true));
        assertThat(updatePage.getBreadcrumb().size(), is(3));
        assertThat(updatePage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(updatePage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(updatePage.getBreadcrumb().get(1).getLabel(), is("first"));
        // не содержит :page_test_id
        assertThat(updatePage.getBreadcrumb().get(1).getPath(), is("/page/123/view2"));
        assertThat(updatePage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(updatePage.getBreadcrumb().get(2).getPath(), nullValue());
    }

    @Test
    void breadcrumbWithRefWidget() {
        // проверка breadcrumb при открытии open-page из таблицы
        DataSet data = new DataSet().add("parent_id", 123);
        PageContext context = new PageContext("testOpenPageWithRefWidget", "/page/:parent_id/view");
        context.setBreadcrumbs(Collections.singletonList(new Breadcrumb("parent", "/page/:parent_id")));
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageWithRefWidget.page.xml")
                .bind().get(context, data);
        assertThat(page.getRoutes().getSet().contains("/page/:parent_id/view"), is(true));

        Page createPage = routeAndGet("/page/123/view/action1", Page.class);
        assertThat(createPage.getRoutes().getSet().contains("/page/:parent_id/view/action1"), is(true));
        assertThat(createPage.getBreadcrumb().size(), is(3));
        assertThat(createPage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(createPage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(createPage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(createPage.getBreadcrumb().get(1).getPath(), is("/page/123/view"));
        assertThat(createPage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(createPage.getBreadcrumb().get(2).getPath(), nullValue());

        Page updatePage = routeAndGet("/page/123/view/456/action2", Page.class);
        assertThat(updatePage.getRoutes().getSet().contains("/page/:parent_id/view/:page_test_id/action2"), is(true));
        assertThat(updatePage.getBreadcrumb().size(), is(3));
        assertThat(updatePage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(updatePage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(updatePage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(updatePage.getBreadcrumb().get(1).getPath(), is("/page/123/view"));
        assertThat(updatePage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(updatePage.getBreadcrumb().get(2).getPath(), nullValue());

        HashMap<String, String[]> params = new HashMap<>();
        data.put("name", "ivan");
        data.put("secondName", "ivanov");
        Page masterDetailPage = routeAndGet("/page/123/view/456/masterDetail", Page.class, params);
        assertThat(masterDetailPage.getRoutes().getSet().contains("/page/:parent_id/view/:page_test_id/masterDetail"), is(true));
        assertThat(masterDetailPage.getBreadcrumb().size(), is(3));
        assertThat(masterDetailPage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(masterDetailPage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(masterDetailPage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(masterDetailPage.getBreadcrumb().get(1).getPath(), is("/page/123/view"));
        assertThat(masterDetailPage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(masterDetailPage.getBreadcrumb().get(2).getPath(), nullValue());

        Page level3Page = routeAndGet("/page/123/view/456/masterDetail/level3", Page.class, params);
        assertThat(level3Page.getBreadcrumb().size(), is(4));
        assertThat(level3Page.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(level3Page.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(level3Page.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(level3Page.getBreadcrumb().get(1).getPath(), is("/page/123/view"));
        assertThat(level3Page.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(level3Page.getBreadcrumb().get(2).getPath(), is("/page/123/view/456/masterDetail?name=:name&surname=:surname&secondName=test"));

        // проверка breadcrumb при открытии open-page не из таблицы, а другого виджета
        // при этом в parentId не должны сохраняться id выбранных записей
        context = new PageContext("testOpenPageSimplePage2", "/page/:parent_id/view2");
        context.setBreadcrumbs(Collections.singletonList(new Breadcrumb("parent", "/page/:parent_id")));
        Page page2 = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage2.page.xml")
                .bind().get(context, data);

        assertThat(page2.getRoutes().getSet().contains("/page/:parent_id/view2"), is(true));

        updatePage = routeAndGet("/page/123/view2/456/action2", Page.class);
        assertThat(updatePage.getRoutes().getSet().contains("/page/:parent_id/view2/:page_test_id/action2"), is(true));
        assertThat(updatePage.getBreadcrumb().size(), is(3));
        assertThat(updatePage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(updatePage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(updatePage.getBreadcrumb().get(1).getLabel(), is("first"));
        // не содержит :page_test_id
        assertThat(updatePage.getBreadcrumb().get(1).getPath(), is("/page/123/view2"));
        assertThat(updatePage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(updatePage.getBreadcrumb().get(2).getPath(), nullValue());
    }

    @Test
    void masterDetail() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .get(new PageContext("testOpenPageSimplePage", "/page"));

        LinkActionImpl linkAction = (LinkActionImpl) page.getWidget().getToolbar().getButton("masterDetail").getAction();
        assertThat(linkAction.getPathMapping().get("page_test_id").getBindLink(), is("models.resolve['page_test']"));
        assertThat(linkAction.getPathMapping().get("page_test_id").getValue(), is("`id`"));
        assertThat(linkAction.getQueryMapping().get("name").getBindLink(), is("models.filter['page_test']"));
        assertThat(linkAction.getQueryMapping().get("secondName").getBindLink(), nullValue());

        PageContext context = (PageContext) route("/page/gender/masterDetail", Page.class);
        assertThat(context.getPreFilters().size(), is(1));
        assertThat(context.getPreFilters().get(0).getRefPageId(), is("page"));
        assertThat(context.getPreFilters().get(0).getDatasourceId(), is("test"));
        assertThat(context.getPreFilters().get(0).getModel(), is(ReduxModel.resolve));
        assertThat(context.getPreFilters().get(0).getValue(), is("{masterId}"));
        assertThat(context.getPreFilters().get(0).getType(), is(FilterType.eq));

        StandardPage openPage = (StandardPage) read().compile().get(context);
        assertThat(openPage.getId(), is("page_masterDetail"));
        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        Widget openPageWidget = ((Widget) openPage.getRegions().get("single").get(0).getContent().get(0));
        StandardDatasource ds = (StandardDatasource) openPage.getDatasources().get(openPageWidget.getDatasource());
        assertThat(ds.getProvider().getQueryMapping().get("name").getValue(), is("`name`"));
        assertThat(ds.getProvider().getQueryMapping().get("name").getBindLink(), is("models.filter['page_test']"));
        assertThat(ds.getProvider().getQueryMapping().get("surname").getValue(), is("`surname`"));
        assertThat(ds.getProvider().getQueryMapping().get("surname").getBindLink(), is("models.filter['page_test']"));
        assertThat(ds.getProvider().getQueryMapping().get("secondName").getValue(), is("test"));
        assertThat(ds.getProvider().getQueryMapping().get("secondName").getBindLink(), nullValue());
        assertThat(ds.getProvider().getPathMapping().get("page_test_id").normalizeLink(), is("models.resolve['page_test'].masterId"));

        PageContext detailContext = (PageContext) route("/page/gender/masterDetail", Page.class);
        assertThat(detailContext.getQueryRouteMapping().size(), is(3));
        DataSet data = new DataSet();
        data.put("detailId", 222);
        data.put("name", "testName");
        data.put("surname", "Ivanov");
        StandardPage detailPage = (StandardPage) read().compile().bind().get(detailContext, data);
        assertThat(detailPage.getRoutes().getSet().contains("/page/:page_test_id/masterDetail"), is(true));
        assertThat(detailPage.getRoutes().getSet().contains("/page/:page_test_id/masterDetail"), is(true));
        Widget detailPageWidget = (Widget) detailPage.getRegions().get("single").get(0).getContent().get(0);
        Map<String, ModelLink> queryMapping = ((StandardDatasource) detailPage.getDatasources().get(detailPageWidget.getDatasource())).getProvider().getQueryMapping();
        assertThat(queryMapping.get("name").getValue(), is("testName"));
        assertThat(queryMapping.get("surname").getValue(), is("Ivanov"));
        assertThat(queryMapping.get("secondName").getValue(), is("test"));

        assertThat(context.getQueryRouteMapping().get("surname").getValue(), is("`surname`"));
        assertThat(context.getQueryRouteMapping().get("surname").getBindLink(), is("models.filter['page_test']"));
    }

    @Test
    void dynamicPage() {
        compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageDynamicPage.page.xml")
                .get(new PageContext("testOpenPageDynamicPage", "/page"));
        PageContext context = (PageContext) route("/page/testOpenPageSimplePageAction1/id1", Page.class);
        DataSet data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction1");
        SimplePage openPage = (SimplePage) read().compile().bind().get(context, data);

        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        assertThat(openPage.getWidget().getId(), is("page_testOpenPageSimplePageAction1_id1_w1"));
        assertThat(openPage.getRoutes().getSet().iterator().next(), is("/page/testOpenPageSimplePageAction1/id1"));
        assertThat(openPage.getWidget(), instanceOf(Form.class));

        context = (PageContext) route("/page/testOpenPageSimplePageAction2/id1", Page.class);
        data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction2");
        openPage = (SimplePage) read().compile().bind().get(context, data);

        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second2"));

        assertThat(openPage.getWidget().getId(), is("page_testOpenPageSimplePageAction2_id1_main"));

        assertThat(openPage.getRoutes().getSet().iterator().next(), is("/page/testOpenPageSimplePageAction2/id1"));

        assertThat(openPage.getWidget(), instanceOf(Form.class));
    }

    @Test
    void testMasterParam() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline =
                compile("net/n2oapp/framework/config/metadata/compile/action/testMasterParam.page.xml",
                        "net/n2oapp/framework/config/metadata/compile/action/testOpenPageMasterParam.page.xml");

        Page p1 = pipeline.get(new PageContext("testMasterParam", "/page"));
        assertThat(p1.getRoutes().getSet().contains("/page"), is(true));

        StandardPage p2 = (StandardPage) pipeline.get(new PageContext("testOpenPageMasterParam"));
        assertThat(((StandardDatasource) p2.getDatasources().get("testOpenPageMasterParam_form")).getProvider().getQueryMapping().size(), is(0));
        assertThat(((StandardDatasource) p2.getDatasources().get("testOpenPageMasterParam_form")).getProvider().getUrl(), is("n2o/data/testOpenPageMasterParam"));
        assertThat(((StandardDatasource) p2.getDatasources().get("testOpenPageMasterParam_modalDetail")).getProvider().getQueryMapping().size(), is(1));
        assertThat(((StandardDatasource) p2.getDatasources().get("testOpenPageMasterParam_modalDetail")).getProvider().getUrl(), is("n2o/data/testOpenPageMasterParam/detail2"));
        assertThat(p2.getRoutes().getSet().contains("/testOpenPageMasterParam"), is(true));

        ShowModal showModal = (ShowModal) ((Form) p2.getRegions().get("single").get(0).getContent().get(0)).getToolbar().getButton("byName").getAction();
        assertThat(showModal.getPayload().getPageUrl(), is("/testOpenPageMasterParam/byName"));
        Map<String, ModelLink> pathMapping = showModal.getPayload().getPathMapping();
        Map<String, ModelLink> queryMapping = showModal.getPayload().getQueryMapping();

        assertThat(pathMapping.size(), is(0));
        assertThat(queryMapping.size(), is(0));
    }

    @Test
    void testDefaultParam() {
        compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .get(new PageContext("testOpenPageSimplePage", "/page"));
        PageContext context = (PageContext) route("/page/defaultValue", Page.class);
        SimplePage openPage = (SimplePage) read().compile().get(context);
        Map<String, PageRoutes.Query> queryMapping = openPage.getRoutes().getQueryMapping();
        assertThat(queryMapping.size(), is(4)); //у 4 полей на странице testDefaultValue есть param, они добавились в page routes
        ReduxAction onGet = queryMapping.get("name").getOnGet();
        UpdateModelPayload payload = (UpdateModelPayload) onGet.getPayload();
        assertThat(payload.getPrefix(), is("resolve"));
        assertThat(payload.getKey(), is("page_defaultValue_w1"));
        assertThat(payload.getField(), is("surname"));
        assertThat(payload.getValue(), is(":name"));
        assertThat(queryMapping.get("name").getOnSet().getBindLink(), is("models.resolve['page_defaultValue_w1'].surname"));
        payload = (UpdateModelPayload) queryMapping.get("gender_id").getOnGet().getPayload();
        assertThat(payload.getPrefix(), is("resolve"));
        assertThat(payload.getKey(), is("page_defaultValue_w1"));
        assertThat(payload.getField(), is("gender.id"));
        assertThat(payload.getValue(), is(":gender_id"));
        assertThat(queryMapping.get("gender_id").getOnSet().getBindLink(), is("models.resolve['page_defaultValue_w1'].gender"));

        payload = (UpdateModelPayload) queryMapping.get("start").getOnGet().getPayload();
        assertThat(payload.getField(), is("birthDate.begin"));
        assertThat(payload.getValue(), is(":start"));
        assertThat(queryMapping.get("start").getOnSet().getBindLink(), is("models.resolve['page_defaultValue_w1'].birthDate.begin"));

        payload = (UpdateModelPayload) queryMapping.get("end").getOnGet().getPayload();
        assertThat(payload.getField(), is("birthDate.end"));
        assertThat(payload.getValue(), is(":end"));
        assertThat(queryMapping.get("end").getOnSet().getBindLink(), is("models.resolve['page_defaultValue_w1'].birthDate.end"));

        DataSet data = new DataSet();
        data.put("detailId", 222);
        data.put("start", "2022-02-14T00:00:00");
        data.put("end", "2022-03-20T00:00:00");
        data.put("name", "testName");
        data.put("surname", "Ivanov");
        data.put("gender_id", 1);
        openPage = (SimplePage) read().compile().bind().get(context, data);
        assertThat(openPage.getModels().get("resolve['page_defaultValue_w1'].surname").getValue(), is("testName"));
        assertThat(openPage.getModels().get("resolve['page_defaultValue_w1'].birthDate.begin").getValue(), is("2022-02-14T00:00:00"));
        assertThat(openPage.getModels().get("resolve['page_defaultValue_w1'].birthDate.end").getValue(), is("2022-03-20T00:00:00"));
        assertThat(((DefaultValues) openPage.getModels().get("resolve['page_defaultValue_w1'].birthDate")
                .getValue()).getValues().get("begin"), is("2019-02-16T00:00:00"));
        assertThat(((DefaultValues) openPage.getModels().get("resolve['page_defaultValue_w1'].gender").getValue()).getValues().get("id"), is(1));

        context = (PageContext) route("/page/defaultValueQuery", Page.class);
        openPage = (SimplePage) read().compile().get(context);
        queryMapping = openPage.getRoutes().getQueryMapping();
        assertThat(queryMapping.size(), is(4));//у 4 полей на странице testDefaultValue есть param, они добавились в page routes, даже когда datasource формы в режиме query

        context = (PageContext) route("/page/testPreFilter", Page.class);
        openPage = (SimplePage) read().compile().get(context);
        Map<String, ModelLink> queryMapping1 = ((InputSelect) ((StandardField) ((Form) openPage.getWidget()).getComponent()
                .getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0)).getControl()).getDataProvider().getQueryMapping();
        assertThat(queryMapping1.size(), is(1));
        assertThat(queryMapping1.get("id").getValue(), is(1));
    }

    @Test
    void testPathParam() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageWithPathParam.page.xml")
                .get(new PageContext("testOpenPageWithPathParam", "/page"));

        PerformButton btn1 = (PerformButton) ((Widget) page.getRegions().get("single").get(0).getContent().get(0)).getToolbar().getButton("btn1");
        assertThat(((LinkAction) btn1.getAction()).getPathMapping().size(), is(1));
        assertThat(((LinkAction) btn1.getAction()).getPathMapping().get("client_id").getBindLink(), nullValue());
        assertThat(((LinkAction) btn1.getAction()).getPathMapping().get("client_id").getValue(), is(123));

        PerformButton btn2 = (PerformButton) ((Widget) page.getRegions().get("single").get(0).getContent().get(0)).getToolbar().getButton("btn2");
        assertThat(((LinkAction) btn2.getAction()).getPathMapping().size(), is(1));
        assertThat(((LinkAction) btn2.getAction()).getPathMapping().get("account_id").getBindLink(), is("models.resolve['page_master']"));
        assertThat(((LinkAction) btn2.getAction()).getPathMapping().get("account_id").getValue(), is("`accountId`"));
        ModelLink link = page.getModels().get("resolve['page_master'].accountId");
        assertThat(link.getBindLink(), is("models.resolve['page_master']"));
        assertThat(link.getValue(), is(111));
    }


    @Test
    void testPathParamValidation() {
        assertThrows(
                N2oException.class,
                () -> compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageWithExpectPathParam.page.xml")
                        .get(new PageContext("testOpenPageWithExpectPathParam", "/page"))
        );
    }


    @Test
    void testBinding() {
        compile("net/n2oapp/framework/config/metadata/compile/action/testBindOpenPage.page.xml",
                "net/n2oapp/framework/config/metadata/compile/action/testBindOpenPage.query.xml",
                "net/n2oapp/framework/config/metadata/compile/action/testBindOpenPageShow.page.xml")
                .get(new PageContext("testBindOpenPage", "/page"));
        PageContext context = (PageContext) route("/page/show", Page.class);
        DataSet data = new DataSet();
        data.put("name", "test");
        SimplePage openPage = (SimplePage) read().compile().bind().get(context, data);
        ClientDataProvider provider = ((StandardDatasource) openPage.getDatasources().get(openPage.getWidget().getDatasource())).getProvider();
        assertThat(provider.getUrl(), is("n2o/data/page/show/w1"));
        assertThat(provider.getQueryMapping().size(), is(1));
        assertThat(provider.getQueryMapping().get("name").isConst(), is(true));

        compile("net/n2oapp/framework/config/metadata/compile/action/testBindOpenPageShow.page.xml",
                "net/n2oapp/framework/config/metadata/compile/action/testBindOpenPage.query.xml")
                .get(new PageContext("testBindOpenPageShow", "/testBind"));
        context = (PageContext) route("/testBind", Page.class);
        openPage = (SimplePage) read().compile().bind().get(context, data);
        provider = ((StandardDatasource) openPage.getDatasources().get(openPage.getWidget().getDatasource())).getProvider();
        assertThat(provider.getUrl(), is("n2o/data/testBind/w1"));
        assertThat(provider.getQueryMapping().size(), is(1));
        assertThat(provider.getQueryMapping().get("name").getValue(), is("test"));
    }

    @Test
    void link() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testSimpleOpenPage.page.xml")
                .get(new PageContext("testSimpleOpenPage"));
        Toolbar toolbar = page.getWidget().getToolbar();
        LinkAction link = (LinkAction) toolbar.getButton("id1").getAction();
        assertThat(link.getUrl(), is("/testSimpleOpenPage/id1"));
        assertThat(link.getTarget(), is(Target.application));

        link = (LinkAction) toolbar.getButton("id2").getAction();
        assertThat(link.getUrl(), is("#/testSimpleOpenPage/view"));
        assertThat(link.getTarget(), is(Target.newWindow));
    }

    @Test
    void testCustomBreadcrumb() {
        DataSet data = new DataSet("id", 123);
        PageContext context = new PageContext("testOpenPageParent", "/page/:id/view");
        ModelLink modelLink = new ModelLink(ReduxModel.resolve, "ds1");
        modelLink.setValue("`id`");
        context.setPathRouteMapping(Collections.singletonMap("id", modelLink));
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageParent.page.xml")
                .bind().get(context, data);
        assertThat(page.getRoutes().getSet().contains("/page/:id/view"), is(true));

        Page openPage = routeAndGet("/page/123/view/open", Page.class);
        assertThat(openPage.getBreadcrumb().size(), is(3));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("First"));
        assertThat(openPage.getBreadcrumb().get(0).getPath(), is("/"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("Second"));
        assertThat(openPage.getBreadcrumb().get(1).getPath(), is("/123/page2"));
        assertThat(openPage.getBreadcrumb().get(2).getLabel(), is("Third 123"));
        assertThat(openPage.getBreadcrumb().get(2).getPath(), nullValue());
        assertThat(openPage.getPageProperty().getTitle(), is("Id 123"));
        assertThat(openPage.getPageProperty().getHtmlTitle(), is("Id 123"));
    }

    /**
     * Проверяет переопределение toolbar и action
     */
    @Test
    void testOpenPageToolbarAndActions() {
        PageContext pageContext = new PageContext("testOpenPageToolbarAndAction", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageToolbarAndAction.page.xml")
                .get(pageContext);

        StandardPage page = (StandardPage) routeAndGet("/p/create", Page.class);
        List<Group> toolbar = page.getToolbar().get("bottomCenter");
        assertThat(toolbar.get(0).getButtons().size(), is(1));
        assertThat(toolbar.get(0).getButtons().get(0).getLabel(), is("Button in center"));
        assertThat(toolbar.get(0).getButtons().get(0).getAction(), instanceOf(LinkAction.class));
        assertThat(((LinkAction) toolbar.get(0).getButtons().get(0).getAction()).getUrl(), is("http://i-novus.ru"));

        page = (StandardPage) routeAndGet("/p/update", Page.class);
        toolbar = page.getToolbar().get("bottomCenter");
        assertThat(toolbar.get(0).getButtons().size(), is(1));
        assertThat(toolbar.get(0).getButtons().get(0).getLabel(), is("Button in center"));
        assertThat(toolbar.get(0).getButtons().get(0).getAction(), instanceOf(LinkAction.class));
        assertThat(((LinkAction) toolbar.get(0).getButtons().get(0).getAction()).getUrl(), is("http://i-novus.ru"));
        toolbar = page.getToolbar().get("bottomRight");
        assertThat(toolbar.get(0).getButtons().size(), is(1));
        assertThat(toolbar.get(0).getButtons().get(0).getLabel(), is("Button2"));
        assertThat(toolbar.get(0).getButtons().get(0).getAction(), instanceOf(ClearAction.class));
    }
}
