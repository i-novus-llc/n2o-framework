package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.datasource.Datasource;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.*;
import net.n2oapp.framework.api.metadata.meta.action.SelectedWidgetPayload;
import net.n2oapp.framework.api.metadata.meta.action.UpdateModelPayload;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.control.InputSelect;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.saga.AsyncMetaSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
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
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Проверка компиляции open-page
 */
public class OpenPageCompileTest extends SourceCompileTestBase {
    @Override
    @Before
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
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction1.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction2.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageMasterDetail.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testDefaultValue.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testPreFilter.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageWithRefWidget.widget.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testRefbook.query.xml"));
    }

    @Test
    public void filterModel() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .get(new PageContext("testOpenPageSimplePage"));
        PageContext context = (PageContext) route("/page/action1", Page.class);
        SimplePage openPage = (SimplePage) read().compile().get(context);

        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        InvokeAction submit = (InvokeAction) openPage.getToolbar().getButton("submit").getAction();
        InvokeActionPayload submitPayload = submit.getPayload();
        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/page/action1/submit"));
        assertThat(submitPayload.getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(submitPayload.getModel(), is(ReduxModel.RESOLVE));
        assertThat(submitPayload.getDatasource(), is("page_action1_main"));
        AsyncMetaSaga meta = submit.getMeta();
        assertThat(meta.getSuccess().getRefresh().getDatasources(), hasItem("page_test"));
        assertThat(meta.getSuccess().getMessageWidgetId(), is("page_test"));
        assertThat(meta.getSuccess().getModalsToClose(), nullValue());
        assertThat(meta.getSuccess().getRedirect().getPath(), is("/page"));
        ActionContext submitContext = (ActionContext) route("/page/action1/submit", CompiledObject.class);
        assertThat(submitContext.getRedirect(), nullValue());

        LinkActionImpl close = (LinkActionImpl) openPage.getToolbar().getButton("close").getAction();
        assertThat(close.getUrl(), is("/page"));
        assertThat(close.getTarget(), is(Target.application));

        PageRoutes.Route action1 = page.getRoutes().findRouteByUrl("/page/action1");
        assertThat(action1.getIsOtherPage(), is(true));
    }

    @Test
    public void resolveModel() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml")
                .get(new PageContext("testOpenPageSimplePage", "/page"));
        LinkActionImpl action = (LinkActionImpl) page.getWidget().getToolbar().getButton("id2").getAction();
        assertThat(action.getPathMapping().get("page_test_id").getBindLink(), is("models.resolve['page_test']"));
        assertThat(action.getPathMapping().get("page_test_id").getValue(), is("`id`"));
        assertThat(action.getQueryMapping().size(), is(0));

        PageContext context = (PageContext) route("/page/widget/123/action2", Page.class);
        assertThat(context.getPreFilters().size(), is(1));
        assertThat(context.getPreFilters().get(0).getRefPageId(), is("page"));
        assertThat(context.getPreFilters().get(0).getRefWidgetId(), is("test"));
        assertThat(context.getPreFilters().get(0).getModel(), is(ReduxModel.RESOLVE));
        assertThat(context.getPreFilters().get(0).getParam(), is("page_test_id"));
        assertThat(context.getPreFilters().get(0).getType(), is(FilterType.eq));
        assertThat(context.getParentModelLink().getFieldId(), is("id"));
        assertThat(context.getParentModelLink().getDatasource(), is("page_test"));
        assertThat(context.getParentModelLink().getSubModelQuery(), nullValue());

        SimplePage openPage = (SimplePage) read().compile().get(context);
        assertThat(openPage.getId(), is("page_widget_action2"));
        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        assertThat(((Filter) openPage.getWidget().getFilters().get(0)).getParam(), is("page_test_id"));
        assertThat(((Filter) openPage.getWidget().getFilters().get(0)).getFilterId(), is("id"));
        assertThat(((Filter) openPage.getWidget().getFilters().get(0)).getLink().getBindLink(), is("models.resolve['page_test']"));
        assertThat(((Filter) openPage.getWidget().getFilters().get(0)).getLink().getValue(), is("`id`"));

        Map<String, ReduxAction> pathMapping = openPage.getRoutes().getPathMapping();
        assertThat(pathMapping.size(), is(1));
        assertThat(pathMapping.get("page_widget_action2_main_id").getType(), is("n2o/widgets/CHANGE_SELECTED_ID"));
        assertThat(((SelectedWidgetPayload) pathMapping.get("page_widget_action2_main_id").getPayload()).getWidgetId(), is("page_widget_action2_main"));
        assertThat(((SelectedWidgetPayload) pathMapping.get("page_widget_action2_main_id").getPayload()).getValue(), is(":page_widget_action2_main_id"));

        InvokeAction submit = (InvokeAction) openPage.getToolbar().getButton("submit").getAction();
        InvokeActionPayload submitPayload = submit.getPayload();
        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/page/widget/:page_test_id/action2/submit"));
        assertThat(submitPayload.getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(submitPayload.getModel(), is(ReduxModel.RESOLVE));
        assertThat(submitPayload.getDatasource(), is("page_widget_action2_main"));
        AsyncMetaSaga meta = submit.getMeta();
        assertThat(meta.getSuccess().getRefresh().getDatasources(), hasItem("page_test"));
        assertThat(meta.getSuccess().getModalsToClose(), nullValue());
        assertThat(meta.getSuccess().getRedirect().getPath(), is("/page/widget/:page_test_id"));
        ActionContext submitContext = (ActionContext) route("/page/widget/123/action2/submit", CompiledObject.class);
        assertThat(submitContext.getRedirect(), nullValue());

        LinkActionImpl close = (LinkActionImpl) openPage.getToolbar().getButton("close").getAction();
        assertThat(close.getUrl(), is("/page/widget/:page_test_id"));
        assertThat(close.getTarget(), is(Target.application));

        Widget modalWidget = openPage.getWidget();
        List<Filter> preFilters = modalWidget.getFilters();
        assertThat(preFilters.get(0).getFilterId(), is("id"));
        assertThat(preFilters.get(0).getParam(), is("page_test_id"));
        PageRoutes.Route action2 = page.getRoutes().findRouteByUrl("/page/widget/:page_test_id/action2");
        assertThat(action2.getIsOtherPage(), is(true));
    }

    @Test
    public void breadcrumb() {
        // проверка breadcrumb при открытии open-page из таблицы
        DataSet data = new DataSet().add("parent_id", 123);
        PageContext context = new PageContext("testOpenPageSimplePage", "/page/:parent_id/view");
        context.setBreadcrumbs(Collections.singletonList(new Breadcrumb("parent", "/page/:parent_id")));
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .bind().get(context, data);
        assertThat(page.getRoutes().findRouteByUrl("/page/123/view"), notNullValue());

        Page createPage = routeAndGet("/page/123/view/widget/action1", Page.class);
        assertThat(createPage.getRoutes().findRouteByUrl("/page/123/view/widget/action1"), notNullValue());
        assertThat(createPage.getBreadcrumb().size(), is(3));
        assertThat(createPage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(createPage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(createPage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(createPage.getBreadcrumb().get(1).getPath(), is("/page/123/view/widget"));
        assertThat(createPage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(createPage.getBreadcrumb().get(2).getPath(), nullValue());

        Page updatePage = routeAndGet("/page/123/view/widget/456/action2", Page.class);
        assertThat(updatePage.getRoutes().findRouteByUrl("/page/123/view/widget/456/action2"), notNullValue());
        assertThat(updatePage.getBreadcrumb().size(), is(3));
        assertThat(updatePage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(updatePage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(updatePage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(updatePage.getBreadcrumb().get(1).getPath(), is("/page/123/view/widget/456"));
        assertThat(updatePage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(updatePage.getBreadcrumb().get(2).getPath(), nullValue());

        HashMap<String, String[]> params = new HashMap<>();
        data.put("name", "ivan");
        data.put("secondName", "ivanov");
        Page masterDetailPage = routeAndGet("/page/123/view/widget/456/masterDetail", Page.class, params);
        assertThat(masterDetailPage.getRoutes().findRouteByUrl("/page/123/view/widget/456/masterDetail"), notNullValue());
        assertThat(masterDetailPage.getBreadcrumb().size(), is(3));
        assertThat(masterDetailPage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(masterDetailPage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(masterDetailPage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(masterDetailPage.getBreadcrumb().get(1).getPath(), is("/page/123/view/widget/456"));
        assertThat(masterDetailPage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(masterDetailPage.getBreadcrumb().get(2).getPath(), nullValue());

        Page level3Page = routeAndGet("/page/123/view/widget/456/masterDetail/level3", Page.class, params);
        assertThat(level3Page.getBreadcrumb().size(), is(4));
        assertThat(level3Page.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(level3Page.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(level3Page.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(level3Page.getBreadcrumb().get(1).getPath(), is("/page/123/view/widget/456"));
        assertThat(level3Page.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(level3Page.getBreadcrumb().get(2).getPath(), is("/page/123/view/widget/456/masterDetail?name=:name&surname=:surname&secondName=test"));

        // проверка breadcrumb при открытии open-page не из таблицы, а другого виджета
        // при этом в parentId не должны сохраняться id выбранных записей
        context = new PageContext("testOpenPageSimplePage2", "/page/:parent_id/view2");
        context.setBreadcrumbs(Collections.singletonList(new Breadcrumb("parent", "/page/:parent_id")));
        Page page2 = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage2.page.xml")
                .bind().get(context, data);

        assertThat(page2.getRoutes().findRouteByUrl("/page/123/view2"), notNullValue());

        updatePage = routeAndGet("/page/123/view2/widget/456/action2", Page.class);
        assertThat(updatePage.getRoutes().findRouteByUrl("/page/123/view2/widget/456/action2"), notNullValue());
        assertThat(updatePage.getBreadcrumb().size(), is(3));
        assertThat(updatePage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(updatePage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(updatePage.getBreadcrumb().get(1).getLabel(), is("first"));
        // не содержит :page_test_id
        assertThat(updatePage.getBreadcrumb().get(1).getPath(), is("/page/123/view2/widget"));
        assertThat(updatePage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(updatePage.getBreadcrumb().get(2).getPath(), nullValue());
    }

    @Test
    public void breadcrumbWithRefWidget() {
        // проверка breadcrumb при открытии open-page из таблицы
        DataSet data = new DataSet().add("parent_id", 123);
        PageContext context = new PageContext("testOpenPageWithRefWidget", "/page/:parent_id/view");
        context.setBreadcrumbs(Collections.singletonList(new Breadcrumb("parent", "/page/:parent_id")));
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageWithRefWidget.page.xml")
                .bind().get(context, data);
        assertThat(page.getRoutes().findRouteByUrl("/page/123/view"), notNullValue());

        Page createPage = routeAndGet("/page/123/view/action1", Page.class);
        assertThat(createPage.getRoutes().findRouteByUrl("/page/123/view/action1"), notNullValue());
        assertThat(createPage.getBreadcrumb().size(), is(3));
        assertThat(createPage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(createPage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(createPage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(createPage.getBreadcrumb().get(1).getPath(), is("/page/123/view"));
        assertThat(createPage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(createPage.getBreadcrumb().get(2).getPath(), nullValue());

        Page updatePage = routeAndGet("/page/123/view/456/action2", Page.class);
        assertThat(updatePage.getRoutes().findRouteByUrl("/page/123/view/456/action2"), notNullValue());
        assertThat(updatePage.getBreadcrumb().size(), is(3));
        assertThat(updatePage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(updatePage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(updatePage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(updatePage.getBreadcrumb().get(1).getPath(), is("/page/123/view/456"));
        assertThat(updatePage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(updatePage.getBreadcrumb().get(2).getPath(), nullValue());

        HashMap<String, String[]> params = new HashMap<>();
        data.put("name", "ivan");
        data.put("secondName", "ivanov");
        Page masterDetailPage = routeAndGet("/page/123/view/456/masterDetail", Page.class, params);
        assertThat(masterDetailPage.getRoutes().findRouteByUrl("/page/123/view/456/masterDetail"), notNullValue());
        assertThat(masterDetailPage.getBreadcrumb().size(), is(3));
        assertThat(masterDetailPage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(masterDetailPage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(masterDetailPage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(masterDetailPage.getBreadcrumb().get(1).getPath(), is("/page/123/view/456"));
        assertThat(masterDetailPage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(masterDetailPage.getBreadcrumb().get(2).getPath(), nullValue());

        Page level3Page = routeAndGet("/page/123/view/456/masterDetail/level3", Page.class, params);
        assertThat(level3Page.getBreadcrumb().size(), is(4));
        assertThat(level3Page.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(level3Page.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(level3Page.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(level3Page.getBreadcrumb().get(1).getPath(), is("/page/123/view/456"));
        assertThat(level3Page.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(level3Page.getBreadcrumb().get(2).getPath(), is("/page/123/view/456/masterDetail?name=:name&surname=:surname&secondName=test"));

        // проверка breadcrumb при открытии open-page не из таблицы, а другого виджета
        // при этом в parentId не должны сохраняться id выбранных записей
        context = new PageContext("testOpenPageSimplePage2", "/page/:parent_id/view2");
        context.setBreadcrumbs(Collections.singletonList(new Breadcrumb("parent", "/page/:parent_id")));
        Page page2 = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage2.page.xml")
                .bind().get(context, data);

        assertThat(page2.getRoutes().findRouteByUrl("/page/123/view2"), notNullValue());

        updatePage = routeAndGet("/page/123/view2/widget/456/action2", Page.class);
        assertThat(updatePage.getRoutes().findRouteByUrl("/page/123/view2/widget/456/action2"), notNullValue());
        assertThat(updatePage.getBreadcrumb().size(), is(3));
        assertThat(updatePage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(updatePage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(updatePage.getBreadcrumb().get(1).getLabel(), is("first"));
        // не содержит :page_test_id
        assertThat(updatePage.getBreadcrumb().get(1).getPath(), is("/page/123/view2/widget"));
        assertThat(updatePage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(updatePage.getBreadcrumb().get(2).getPath(), nullValue());
    }

    @Test
    public void masterDetail() {
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
        assertThat(context.getPreFilters().get(0).getRefWidgetId(), is("test"));
        assertThat(context.getPreFilters().get(0).getModel(), is(ReduxModel.RESOLVE));
        assertThat(context.getPreFilters().get(0).getValue(), is("{masterId}"));
        assertThat(context.getPreFilters().get(0).getType(), is(FilterType.eq));

        StandardPage openPage = (StandardPage) read().compile().get(context);
        assertThat(openPage.getId(), is("page_masterDetail"));
        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        Widget openPageWidget = ((Widget) openPage.getRegions().get("single").get(0).getContent().get(0));
        Datasource ds = openPage.getDatasources().get(openPageWidget.getDatasource());
        assertThat(ds.getProvider().getQueryMapping().get("name").getValue(), is("`name`"));
        assertThat(ds.getProvider().getQueryMapping().get("name").getBindLink(), is("models.filter['page_test']"));
        assertThat(ds.getProvider().getQueryMapping().get("surname").getValue(), is("`surname`"));
        assertThat(ds.getProvider().getQueryMapping().get("surname").getBindLink(), is("models.filter['page_test']"));
        assertThat(ds.getProvider().getQueryMapping().get("secondName").getValue(), is("test"));
        assertThat(ds.getProvider().getQueryMapping().get("secondName").getBindLink(), nullValue());
        assertThat(ds.getProvider().getPathMapping().get("page_test_id").getValue(), is("`id`"));
        assertThat(ds.getProvider().getPathMapping().get("page_test_id").getBindLink(), is("models.resolve['page_test']"));

        Map<String, ReduxAction> pathMapping = openPage.getRoutes().getPathMapping();
        assertThat(pathMapping.size(), is(1));
        assertThat(pathMapping.get("page_masterDetail_main_id").getType(), is("n2o/widgets/CHANGE_SELECTED_ID"));
        assertThat(((SelectedWidgetPayload) pathMapping.get("page_masterDetail_main_id")
                .getPayload()).getWidgetId(), is("page_masterDetail_main"));
        assertThat(((SelectedWidgetPayload) pathMapping.get("page_masterDetail_main_id")
                .getPayload()).getValue(), is(":page_masterDetail_main_id"));

        PageContext detailContext = (PageContext) route("/page/gender/masterDetail", Page.class);
        assertThat(detailContext.getQueryRouteMapping().size(), is(3));
        DataSet data = new DataSet();
        data.put("detailId", 222);
        data.put("name", "testName");
        data.put("surname", "Ivanov");
        StandardPage detailPage = (StandardPage) read().compile().bind().get(detailContext, data);
        assertThat(detailPage.getRoutes().findRouteByUrl("/page/:page_test_id/masterDetail"), notNullValue());
        assertThat(detailPage.getRoutes().findRouteByUrl("/page/:page_test_id/masterDetail"), notNullValue());
        Widget detailPageWidget = (Widget) detailPage.getRegions().get("single").get(0).getContent().get(0);
        Map<String, ModelLink> queryMapping = detailPage.getDatasources().get(detailPageWidget.getDatasource()).getProvider().getQueryMapping();
        assertThat(queryMapping.get("name").getValue(), is("testName"));
        assertThat(queryMapping.get("surname").getValue(), is("Ivanov"));
        Filter filter = (Filter) detailPageWidget.getFilters().get(0);
        assertThat(filter.getParam(), is("name"));
        assertThat(filter.getFilterId(), is("name"));
        assertThat(filter.getLink().getValue(), is("`name`"));
        filter = (Filter) detailPageWidget.getFilters().get(1);
        assertThat(filter.getParam(), is("secondName"));
        assertThat(filter.getFilterId(), is("secondName"));
        assertThat(filter.getLink().getValue(), is("test"));
        filter = (Filter) detailPageWidget.getFilters().get(2);
        assertThat(filter.getParam(), is("surname"));
        assertThat(filter.getFilterId(), is("surname"));
        assertThat(filter.getLink().getValue(), is("`surname`"));
        filter = (Filter) detailPageWidget.getFilters().get(3);
        assertThat(filter.getParam(), is("detailId"));
        assertThat(filter.getFilterId(), is("detailId"));
        assertThat(filter.getLink().getValue(), is("`masterId`"));
        assertThat(context.getQueryRouteMapping().get("surname").getValue(), is("`surname`"));
        assertThat(context.getQueryRouteMapping().get("surname").getBindLink(), is("models.filter['page_test']"));
    }

    @Test
    public void dynamicPage() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageDynamicPage.page.xml")
                .get(new PageContext("testOpenPageDynamicPage", "/page"));
        PageContext context = (PageContext) route("/page/testOpenPageSimplePageAction1/id1", Page.class);
        DataSet data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction1");
        SimplePage openPage = (SimplePage) read().compile().bind().get(context, data);

        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        assertThat(openPage.getWidget().getId(), is("page_testOpenPageSimplePageAction1_id1_main"));

        assertThat(openPage.getRoutes().getList().get(0).getPath(), is("/page/testOpenPageSimplePageAction1/id1"));
        assertThat(openPage.getRoutes().getList().get(1).getPath(), is("/page/testOpenPageSimplePageAction1/id1/:page_widget_testOpenPageSimplePageAction1_id1_main_id"));

        assertThat(openPage.getWidget(), instanceOf(Form.class));

        context = (PageContext) route("/page/testOpenPageSimplePageAction2/id1", Page.class);
        data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction2");
        openPage = (SimplePage) read().compile().bind().get(context, data);

        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second2"));

        assertThat(openPage.getWidget().getId(), is("page_testOpenPageSimplePageAction2_id1_main"));

        assertThat(openPage.getRoutes().getList().get(0).getPath(), is("/page/testOpenPageSimplePageAction2/id1"));
        assertThat(openPage.getRoutes().getList().get(1).getPath(), is("/page/testOpenPageSimplePageAction2/id1/:page_widget_testOpenPageSimplePageAction2_id1_main_id"));

        assertThat(openPage.getWidget(), instanceOf(Form.class));
    }

    @Test
    public void testMasterParam() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline =
                compile("net/n2oapp/framework/config/metadata/compile/action/testMasterParam.page.xml",
                        "net/n2oapp/framework/config/metadata/compile/action/testOpenPageMasterParam.page.xml");

        Page p1 = pipeline.get(new PageContext("testMasterParam", "/page"));
        assertThat(p1.getRoutes().findRouteByUrl("/page"), notNullValue());
        assertThat(p1.getRoutes().findRouteByUrl("/page/master"), notNullValue());
        assertThat(p1.getRoutes().findRouteByUrl("/page/master/:page_master_id"), notNullValue());
        assertThat(p1.getRoutes().findRouteByUrl("/page/master/menuItem0"), notNullValue());
        assertThat(p1.getRoutes().findRouteByUrl("/page/master/:sid/detail"), notNullValue());
        assertThat(p1.getRoutes().findRouteByUrl("/page/master/:sid/detail/:page_detail_id"), notNullValue());
        assertThat(p1.getRoutes().findRouteByUrl("/page/master/:sid/detail/menuItem0"), notNullValue());

        StandardPage p2 = (StandardPage) pipeline.get(new PageContext("testOpenPageMasterParam"));
        assertThat(((Form) p2.getRegions().get("right").get(0).getContent().get(0))
                .getFilters().get(0).getParam(), is("sid"));
        assertThat(p2.getRoutes().findRouteByUrl("/testOpenPageMasterParam/:testOpenPageMasterParam_form_id"), notNullValue());
        assertThat(p2.getRoutes().findRouteByUrl("/testOpenPageMasterParam/detail2/:testOpenPageMasterParam_modalDetail_id"), notNullValue());

        ShowModal showModal = (ShowModal) ((Form) p2.getRegions().get("left").get(0).getContent().get(0)).getToolbar().getButton("byName").getAction();
        assertThat(showModal.getPayload().getPageUrl(), is("/testOpenPageMasterParam/byName"));
        Map<String, ModelLink> pathMapping = showModal.getPayload().getPathMapping();
        Map<String, ModelLink> queryMapping = showModal.getPayload().getQueryMapping();

        assertThat(pathMapping.size(), is(0));
        assertThat(queryMapping.size(), is(0));
    }

    @Test
    public void testDefaultParam() {
        compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .get(new PageContext("testOpenPageSimplePage", "/page"));
        PageContext context = (PageContext) route("/page/widget/defaultValue", Page.class);
        SimplePage openPage = (SimplePage) read().compile().get(context);
        Map<String, PageRoutes.Query> queryMapping = openPage.getRoutes().getQueryMapping();
        assertThat(queryMapping.size(), is(4));
        ReduxAction onGet = queryMapping.get("name").getOnGet();
        UpdateModelPayload payload = (UpdateModelPayload) onGet.getPayload();
        assertThat(payload.getPrefix(), is("resolve"));
        assertThat(payload.getKey(), is("page_widget_defaultValue_main"));
        assertThat(payload.getField(), is("surname"));
        assertThat(payload.getValue(), is(":name"));
        assertThat(queryMapping.get("name").getOnSet().getBindLink(), is("models.resolve['page_widget_defaultValue_main'].surname"));
        payload = (UpdateModelPayload) queryMapping.get("gender_id").getOnGet().getPayload();
        assertThat(payload.getPrefix(), is("resolve"));
        assertThat(payload.getKey(), is("page_widget_defaultValue_main"));
        assertThat(payload.getField(), is("gender.id"));
        assertThat(payload.getValue(), is(":gender_id"));
        assertThat(queryMapping.get("gender_id").getOnSet().getBindLink(), is("models.resolve['page_widget_defaultValue_main'].gender"));

        payload = (UpdateModelPayload) queryMapping.get("start").getOnGet().getPayload();
        assertThat(payload.getField(), is("birthDate.begin"));
        assertThat(payload.getValue(), is(":start"));
        assertThat(queryMapping.get("start").getOnSet().getBindLink(), is("models.resolve['page_widget_defaultValue_main'].birthDate.begin"));

        payload = (UpdateModelPayload) queryMapping.get("end").getOnGet().getPayload();
        assertThat(payload.getField(), is("birthDate.end"));
        assertThat(payload.getValue(), is(":end"));
        assertThat(queryMapping.get("end").getOnSet().getBindLink(), is("models.resolve['page_widget_defaultValue_main'].birthDate.end"));

        DataSet data = new DataSet();
        data.put("detailId", 222);
        data.put("start", "2022-02-14T00:00:00");
        data.put("end", "2022-03-20T00:00:00");
        data.put("name", "testName");
        data.put("surname", "Ivanov");
        data.put("gender_id", 1);
        openPage = (SimplePage) read().compile().bind().get(context, data);
        assertThat(openPage.getModels().get("resolve['page_widget_defaultValue_main'].surname").getValue(), is("testName"));
        assertThat(openPage.getModels().get("resolve['page_widget_defaultValue_main'].birthDate.begin").getValue(), is("2022-02-14T00:00:00"));
        assertThat(openPage.getModels().get("resolve['page_widget_defaultValue_main'].birthDate.end").getValue(), is("2022-03-20T00:00:00"));
        assertThat(((DefaultValues) openPage.getModels().get("resolve['page_widget_defaultValue_main'].birthDate")
                .getValue()).getValues().get("begin"), is("2019-02-16T00:00:00"));
        assertThat(((DefaultValues)openPage.getModels().get("resolve['page_widget_defaultValue_main'].gender").getValue()).getValues().get("id"), is(1));

        context = (PageContext) route("/page/widget/defaultValueQuery", Page.class);
        openPage = (SimplePage) read().compile().get(context);
        queryMapping = openPage.getRoutes().getQueryMapping();
        assertThat(queryMapping.size(), is(0));

        context = (PageContext) route("/page/widget/testPreFilter", Page.class);
        openPage = (SimplePage) read().compile().get(context);
        Map<String, ModelLink> queryMapping1 = ((InputSelect) ((StandardField) ((Form) openPage.getWidget()).getComponent()
                .getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0)).getControl()).getDataProvider().getQueryMapping();
        assertThat(queryMapping1.size(), is(1));
        assertThat(queryMapping1.get("id").getValue(), is(1));
    }

    @Test
    public void testPathParam() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageWithPathParam.page.xml")
                .get(new PageContext("testOpenPageWithPathParam", "/page"));

        PerformButton btn1 = (PerformButton) ((Widget) page.getRegions().get("single").get(0).getContent().get(0)).getToolbar().getButton("btn1");
        assertThat(btn1.getPathMapping().size(), is(1));
        assertThat(btn1.getPathMapping().get("client_id").getBindLink(), nullValue());
        assertThat(btn1.getPathMapping().get("client_id").getValue(), is(123));

        PerformButton btn2 = (PerformButton) ((Widget) page.getRegions().get("single").get(0).getContent().get(0)).getToolbar().getButton("btn2");
        assertThat(btn2.getPathMapping().size(), is(1));
        assertThat(btn2.getPathMapping().get("account_id").getBindLink(), is("models.resolve['page_master']"));
        assertThat(btn2.getPathMapping().get("account_id").getValue(), is("`accountId`"));
        ModelLink link = page.getModels().get("resolve['page_master'].accountId");
        assertThat(link.getBindLink(), is("models.resolve['page_master']"));
        assertThat(link.getValue(), is(111));
    }


    @Test(expected = N2oException.class)
    public void testPathParamValidation() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageWithExpectPathParam.page.xml")
                .get(new PageContext("testOpenPageWithExpectPathParam", "/page"));

    }


    @Test
    public void testBinding() {
        compile("net/n2oapp/framework/config/metadata/compile/action/testBindOpenPage.page.xml",
                "net/n2oapp/framework/config/metadata/compile/action/testBindOpenPage.query.xml",
                "net/n2oapp/framework/config/metadata/compile/action/testBindOpenPageShow.page.xml")
                .get(new PageContext("testBindOpenPage", "/page"));
        PageContext context = (PageContext) route("/page/show", Page.class);
        DataSet data = new DataSet();
        data.put("name", "test");
        SimplePage openPage = (SimplePage) read().compile().bind().get(context, data);
        ClientDataProvider provider = openPage.getDatasources().get(openPage.getWidget().getDatasource()).getProvider();
        assertThat(provider.getUrl(), is("n2o/data/page/show/main"));
        assertThat(provider.getQueryMapping().size(), is(1));
        assertThat(provider.getQueryMapping().get("name").isConst(), is(true));

        compile("net/n2oapp/framework/config/metadata/compile/action/testBindOpenPageShow.page.xml",
                "net/n2oapp/framework/config/metadata/compile/action/testBindOpenPage.query.xml")
                .get(new PageContext("testBindOpenPageShow", "/testBind"));
        context = (PageContext) route("/testBind", Page.class);
        openPage = (SimplePage) read().compile().bind().get(context, data);
        provider = openPage.getDatasources().get(openPage.getWidget().getDatasource()).getProvider();
        assertThat(provider.getUrl(), is("n2o/data/testBind/main"));
        assertThat(provider.getQueryMapping().size(), is(1));
        assertThat(provider.getQueryMapping().get("name").getValue(), is("test"));
    }
}
