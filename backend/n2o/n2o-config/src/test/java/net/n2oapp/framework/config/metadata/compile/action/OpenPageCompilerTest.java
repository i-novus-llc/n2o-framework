package net.n2oapp.framework.config.metadata.compile.action;


import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.*;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkAction;
import net.n2oapp.framework.api.metadata.meta.saga.AsyncMetaSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
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
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Проверка копиляции open-page
 */
public class OpenPageCompilerTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oActionsPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack(), new N2oAllDataPack(), new N2oCellsPack());
        builder.ios(new OpenPageElementIOV1(), new InvokeActionElementIOV1(), new CloseActionElementIOV1());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageDynamicPage.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction1.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction2.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageMasterDetail.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testRefbook.query.xml"));
    }

    @Test
    public void filterModel() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .get(new PageContext("testOpenPageSimplePage"));
        PageContext context = (PageContext) route("/page/widget/action1", Page.class);
        Page openPage = read().compile().get(context);

        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        assertThat(openPage.getActions().size(), is(2));
        InvokeAction submit = (InvokeAction) openPage.getActions().get("submit");
        assertThat(submit.getId(), is("submit"));
        InvokeActionPayload submitPayload = submit.getOptions().getPayload();
        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/page/widget/action1/submit"));
        assertThat(submitPayload.getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(submitPayload.getModelLink(), is("models.resolve['page_widget_action1_main']"));
        assertThat(submitPayload.getWidgetId(), is("page_widget_action1_main"));
        AsyncMetaSaga meta = submit.getOptions().getMeta();
        assertThat(meta.getSuccess().getRefresh().getOptions().getWidgetId(), is("page_test"));
        assertThat(meta.getSuccess().getMessageWidgetId(), is("page_test"));
        assertThat(meta.getSuccess().getCloseLastModal(), nullValue());
        assertThat(meta.getSuccess().getRedirect().getPath(), is("/page/widget"));
        ActionContext submitContext = (ActionContext) route("/page/widget/action1/submit", CompiledObject.class);
        assertThat(submitContext.getRedirect(), nullValue());

        LinkAction close = (LinkAction) openPage.getActions().get("close");
        assertThat(close.getId(), is("close"));
        assertThat(close.getOptions().getPath(), is("/page/widget"));
        assertThat(close.getOptions().getTarget(), is(Target.application));

        PageRoutes.Route action1 = page.getRoutes().findRouteByUrl("/page/widget/action1");
        assertThat(action1.getIsOtherPage(), is(true));
    }


    @Test
    public void resolveModel() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml")
                .get(new PageContext("testOpenPageSimplePage", "/page"));
        assertThat(((LinkAction) page.getWidgets().get("page_test").getActions().get("id2")).getOptions().getPathMapping().get("page_test_id").getBindLink(), is("models.resolve['page_test'].id"));
        assertThat(((LinkAction) page.getWidgets().get("page_test").getActions().get("id2")).getOptions().getQueryMapping().size(), is(0));

        PageContext context = (PageContext) route("/page/widget/123/action2", Page.class);
        assertThat(context.getPreFilters().size(), is(1));
        assertThat(context.getPreFilters().get(0).getRefPageId(), is("page"));
        assertThat(context.getPreFilters().get(0).getRefWidgetId(), is("test"));
        assertThat(context.getPreFilters().get(0).getRefModel(), is(ReduxModel.RESOLVE));
        assertThat(context.getPreFilters().get(0).getParam(), is("page_test_id"));
        assertThat(context.getPreFilters().get(0).getType(), is(FilterType.eq));
        assertThat(context.getParentModelLink().getFieldId(), is("id"));
        assertThat(context.getParentModelLink().getWidgetId(), is("page_test"));
        assertThat(context.getParentModelLink().getSubModelQuery().getQueryId(), is("testShowModal"));

        Page openPage = read().compile().get(context);
        assertThat(openPage.getId(), is("page_widget_action2"));
        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        assertThat(((Filter) openPage.getWidgets().get("page_widget_action2_main").getFilters().get(0)).getParam(), is("page_test_id"));
        assertThat(((Filter) openPage.getWidgets().get("page_widget_action2_main").getFilters().get(0)).getFilterId(), is("id"));
        assertThat(((Filter) openPage.getWidgets().get("page_widget_action2_main").getFilters().get(0)).getLink().getBindLink(), is("models.resolve['page_test']"));
        assertThat(((Filter) openPage.getWidgets().get("page_widget_action2_main").getFilters().get(0)).getLink().getValue(), is("`id`"));

        Map<String, ReduxAction> pathMapping = openPage.getRoutes().getPathMapping();
        assertThat(pathMapping.size(), is(1));
        assertThat(pathMapping.get("page_widget_action2_main_id").getType(), is("n2o/widgets/CHANGE_SELECTED_ID"));
        assertThat(pathMapping.get("page_widget_action2_main_id").getPayload().get("widgetId"), is("page_widget_action2_main"));
        assertThat(pathMapping.get("page_widget_action2_main_id").getPayload().get("value"), is(":page_widget_action2_main_id"));


        assertThat(openPage.getActions().size(), is(2));
        InvokeAction submit = (InvokeAction) openPage.getActions().get("submit");
        assertThat(submit.getId(), is("submit"));
        InvokeActionPayload submitPayload = submit.getOptions().getPayload();
        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/page/widget/:page_test_id/action2/submit"));
        assertThat(submitPayload.getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(submitPayload.getModelLink(), is("models.resolve['page_widget_action2_main']"));
        assertThat(submitPayload.getWidgetId(), is("page_widget_action2_main"));
        AsyncMetaSaga meta = submit.getOptions().getMeta();
        assertThat(meta.getSuccess().getRefresh().getOptions().getWidgetId(), is("page_test"));
        assertThat(meta.getSuccess().getCloseLastModal(), nullValue());
        assertThat(meta.getSuccess().getRedirect().getPath(), is("/page/widget/:page_test_id"));
        ActionContext submitContext = (ActionContext) route("/page/widget/123/action2/submit", CompiledObject.class);
        assertThat(submitContext.getRedirect(), nullValue());

        LinkAction close = (LinkAction) openPage.getActions().get("close");
        assertThat(close.getId(), is("close"));
        assertThat(close.getOptions().getPath(), is("/page/widget/:page_test_id"));
        assertThat(close.getOptions().getTarget(), is(Target.application));

        Widget modalWidget = openPage.getWidgets().get("page_widget_action2_main");
        List<Filter> preFilters = modalWidget.getFilters();
        assertThat(preFilters.get(0).getFilterId(), is("id"));
        assertThat(preFilters.get(0).getParam(), is("page_test_id"));
        PageRoutes.Route action2 = page.getRoutes().findRouteByUrl("/page/widget/:page_test_id/action2");
        assertThat(action2.getIsOtherPage(), is(true));
    }

    @Test
    public void breadcrumb() {
        DataSet data = new DataSet().add("parent_id", 123);
        PageContext context = new PageContext("testOpenPageSimplePage", "/page/:parent_id/view");
        context.setBreadcrumbs(Collections.singletonList(new Breadcrumb("parent", "/page/:parent_id")));
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .bind().get(context, data);
        assertThat(page.getRoutes().getList().get(0).getPath(), is("/page/123/view"));

        Page createPage = routeAndGet("/page/123/view/widget/action1", Page.class);
        assertThat(createPage.getRoutes().getList().get(0).getPath(), is("/page/123/view/widget/action1"));
        assertThat(createPage.getBreadcrumb().size(), is(3));
        assertThat(createPage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(createPage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(createPage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(createPage.getBreadcrumb().get(1).getPath(), is("/page/123/view/widget"));
        assertThat(createPage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(createPage.getBreadcrumb().get(2).getPath(), nullValue());

        Page updatePage = routeAndGet("/page/123/view/widget/456/action2", Page.class);
        assertThat(updatePage.getRoutes().getList().get(0).getPath(), is("/page/123/view/widget/456/action2"));
        assertThat(updatePage.getBreadcrumb().size(), is(3));
        assertThat(updatePage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(updatePage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(updatePage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(updatePage.getBreadcrumb().get(1).getPath(), is("/page/123/view/widget/456"));
        assertThat(updatePage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(updatePage.getBreadcrumb().get(2).getPath(), nullValue());

        data = new DataSet();
        data.put("detailId", "12");
        data.put("name", "ivan");
        data.put("secondName", "ivanov");
        Page masterDetailPage = routeAndGet("/page/123/view/widget/456/masterDetail", Page.class, data);
        assertThat(masterDetailPage.getRoutes().getList().get(0).getPath(), is("/page/123/view/widget/456/masterDetail"));
        assertThat(masterDetailPage.getBreadcrumb().size(), is(3));
        assertThat(masterDetailPage.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(masterDetailPage.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(masterDetailPage.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(masterDetailPage.getBreadcrumb().get(1).getPath(), is("/page/123/view/widget/456"));
        assertThat(masterDetailPage.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(masterDetailPage.getBreadcrumb().get(2).getPath(), nullValue());

        Page level3Page = routeAndGet("/page/123/view/widget/456/masterDetail/level3", Page.class, data);
        assertThat(level3Page.getBreadcrumb().size(), is(4));
        assertThat(level3Page.getBreadcrumb().get(0).getLabel(), is("parent"));
        assertThat(level3Page.getBreadcrumb().get(0).getPath(), is("/page/123"));
        assertThat(level3Page.getBreadcrumb().get(1).getLabel(), is("first"));
        assertThat(level3Page.getBreadcrumb().get(1).getPath(), is("/page/123/view/widget/456"));
        assertThat(level3Page.getBreadcrumb().get(2).getLabel(), is("second"));
        assertThat(level3Page.getBreadcrumb().get(2).getPath(), is("/page/123/view/widget/456/masterDetail?name=ivan&detailId=12&secondName=ivanov"));
    }

    @Test
    public void masterDetail() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .get(new PageContext("testOpenPageSimplePage", "/page"));

        LinkAction linkAction = (LinkAction) page.getWidgets().get("page_test").getActions().get("masterDetail");
        assertThat(linkAction.getOptions().getPathMapping().get("page_test_id").getBindLink(), is("models.resolve['page_test'].id"));
        assertThat(linkAction.getOptions().getQueryMapping().get("detailId").getBindLink(), is("models.resolve['page_test']"));
        assertThat(linkAction.getOptions().getQueryMapping().get("detailId").getValue(), is("`masterId`"));
        assertThat(linkAction.getOptions().getQueryMapping().get("name").getBindLink(), is("models.filter['page_test']"));
        assertThat(linkAction.getOptions().getQueryMapping().get("secondName").getBindLink(), nullValue());

        PageContext context = (PageContext) route("/page/widget/gender/masterDetail", Page.class);
        assertThat(context.getPreFilters().size(), is(3));
        assertThat(context.getPreFilters().get(0).getRefPageId(), is("page"));
        assertThat(context.getPreFilters().get(0).getRefWidgetId(), is("test"));
        assertThat(context.getPreFilters().get(0).getRefModel(), is(ReduxModel.RESOLVE));
        assertThat(context.getPreFilters().get(0).getParam(), is("detailId"));
        assertThat(context.getPreFilters().get(0).getFieldId(), is("detailId"));
        assertThat(context.getPreFilters().get(0).getValue(), is("{masterId}"));
        assertThat(context.getPreFilters().get(0).getType(), is(FilterType.eq));
        assertThat(context.getPreFilters().get(1).getRefPageId(), is("page"));
        assertThat(context.getPreFilters().get(1).getRefWidgetId(), is("test"));
        assertThat(context.getPreFilters().get(1).getRefModel(), is(ReduxModel.FILTER));
        assertThat(context.getPreFilters().get(1).getParam(), is("name"));
        assertThat(context.getPreFilters().get(1).getFieldId(), is("name"));
        assertThat(context.getPreFilters().get(1).getValue(), is("{name}"));
        assertThat(context.getPreFilters().get(1).getType(), is(FilterType.eq));
        assertThat(context.getPreFilters().get(2).getRefPageId(), is("page"));
        assertThat(context.getPreFilters().get(2).getRefWidgetId(), is("test"));
        assertThat(context.getPreFilters().get(2).getRefModel(), is(ReduxModel.RESOLVE));
        assertThat(context.getPreFilters().get(2).getParam(), is("secondName"));
        assertThat(context.getPreFilters().get(2).getFieldId(), is("secondName"));
        assertThat(context.getPreFilters().get(2).getValue(), is("test"));
        assertThat(context.getPreFilters().get(2).getType(), is(FilterType.eq));


        Page openPage = read().compile().get(context);
        assertThat(openPage.getId(), is("page_widget_masterDetail"));
        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        Filter filter = (Filter) openPage.getWidgets().get("page_widget_masterDetail_main").getFilters().get(0);
        assertThat(filter.getParam(), is("detailId"));
        assertThat(filter.getFilterId(), is("detailId"));
        assertThat(filter.getLink().getBindLink(), is("models.resolve['page_test']"));
        assertThat(filter.getLink().getValue(), is("`masterId`"));
        filter = (Filter) openPage.getWidgets().get("page_widget_masterDetail_main").getFilters().get(1);
        assertThat(filter.getParam(), is("name"));
        assertThat(filter.getFilterId(), is("name"));
        assertThat(filter.getLink().getBindLink(), is("models.filter['page_test']"));
        assertThat(filter.getLink().getValue(), is("`name`"));
        filter = (Filter) openPage.getWidgets().get("page_widget_masterDetail_main").getFilters().get(2);
        assertThat(filter.getParam(), is("secondName"));
        assertThat(filter.getFilterId(), is("secondName"));
        assertThat(filter.getLink().getBindLink(), nullValue());
        assertThat(filter.getLink().getValue(), is("test"));

        Map<String, ReduxAction> pathMapping = openPage.getRoutes().getPathMapping();
        assertThat(pathMapping.size(), is(1));
        assertThat(pathMapping.get("page_widget_masterDetail_main_id").getType(), is("n2o/widgets/CHANGE_SELECTED_ID"));
        assertThat(pathMapping.get("page_widget_masterDetail_main_id").getPayload().get("widgetId"), is("page_widget_masterDetail_main"));
        assertThat(pathMapping.get("page_widget_masterDetail_main_id").getPayload().get("value"), is(":page_widget_masterDetail_main_id"));

        PageContext detailContext = (PageContext) route("/page/widget/gender/masterDetail", Page.class);
        assertThat(detailContext.getQueryRouteMapping().size(), is(3));
        DataSet data = new DataSet();
        data.put("detailId", 222);
        data.put("name", "testName");
        Page detailPage = read().compile().bind().get(detailContext, data);
        assertThat(detailPage.getRoutes().getList().get(0).getPath(), is("/page/widget/:page_test_id/masterDetail"));
        assertThat(detailPage.getRoutes().getList().get(0).getPath(), is("/page/widget/:page_test_id/masterDetail"));
        Map<String, BindLink> queryMapping = detailPage.getWidgets().get("page_widget_masterDetail_main").getDataProvider().getQueryMapping();
        assertThat(queryMapping.get("detailId").getValue(), is(222));
        assertThat(queryMapping.get("name").getValue(), is("testName"));
        filter = (Filter) detailPage.getWidgets().get("page_widget_masterDetail_main").getFilters().get(0);
        assertThat(filter.getParam(), is("detailId"));
        assertThat(filter.getFilterId(), is("detailId"));
        assertThat(filter.getLink().getValue(), is(222));
        filter = (Filter) detailPage.getWidgets().get("page_widget_masterDetail_main").getFilters().get(1);
        assertThat(filter.getParam(), is("name"));
        assertThat(filter.getFilterId(), is("name"));
        assertThat(filter.getLink().getValue(), is("testName"));
        filter = (Filter) detailPage.getWidgets().get("page_widget_masterDetail_main").getFilters().get(2);
        assertThat(filter.getParam(), is("secondName"));
        assertThat(filter.getFilterId(), is("secondName"));
        assertThat(filter.getLink().getValue(), is("test"));
    }

    @Test
    public void dynamicPage() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageDynamicPage.page.xml")
                .get(new PageContext("testOpenPageDynamicPage", "/page"));
        PageContext context = (PageContext) route("/page/widget/testOpenPageSimplePageAction1/id1", Page.class);
        DataSet data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction1");
        Page openPage = read().compile().bind().get(context, data);

        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        assertThat(openPage.getLayout().getRegions().get("single").get(0).getItems().get(0).getWidgetId(), is("page_widget_testOpenPageSimplePageAction1_id1_main"));

        assertThat(openPage.getRoutes().getList().get(0).getPath(), is("/page/widget/testOpenPageSimplePageAction1/id1"));
        assertThat(openPage.getRoutes().getList().get(1).getPath(), is("/page/widget/testOpenPageSimplePageAction1/id1/:page_widget_testOpenPageSimplePageAction1_id1_main_id"));

        assertThat(openPage.getWidgets().size(), is(1));
        assertThat(openPage.getWidgets().get("page_widget_testOpenPageSimplePageAction1_id1_main"), instanceOf(Form.class));

        context = (PageContext) route("/page/widget/testOpenPageSimplePageAction2/id1", Page.class);
        data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction2");
        openPage = read().compile().bind().get(context, data);

        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second2"));

        assertThat(openPage.getLayout().getRegions().get("single").get(0).getItems().get(0).getWidgetId(), is("page_widget_testOpenPageSimplePageAction2_id1_main"));

        assertThat(openPage.getRoutes().getList().get(0).getPath(), is("/page/widget/testOpenPageSimplePageAction2/id1"));
        assertThat(openPage.getRoutes().getList().get(1).getPath(), is("/page/widget/testOpenPageSimplePageAction2/id1/:page_widget_testOpenPageSimplePageAction2_id1_main_id"));

        assertThat(openPage.getWidgets().size(), is(1));
        assertThat(openPage.getWidgets().get("page_widget_testOpenPageSimplePageAction2_id1_main"), instanceOf(Form.class));
    }

    @Test
    public void testMasterParam() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = compile("net/n2oapp/framework/config/metadata/compile/action/testMasterParam.page.xml",
                "net/n2oapp/framework/config/metadata/compile/action/testOpenPageMasterParam.page.xml");

        Page p1 = pipeline.get(new PageContext("testMasterParam", "/page"));
        assertThat(p1.getRoutes().getList().get(0).getPath(), is("/page"));
        assertThat(p1.getRoutes().getList().get(1).getPath(), is("/page/master"));
        assertThat(p1.getRoutes().getList().get(2).getPath(), is("/page/master/:page_master_id"));
        assertThat(p1.getRoutes().getList().get(3).getPath(), is("/page/master/:sid/menuItem0"));
        assertThat(p1.getRoutes().getList().get(4).getPath(), is("/page/master/:sid/detail"));
        assertThat(p1.getRoutes().getList().get(5).getPath(), is("/page/master/:sid/detail/:page_detail_id"));
        assertThat(p1.getRoutes().getList().get(6).getPath(), is("/page/master/:sid/detail/:sid/menuItem0"));

        Page p2 = pipeline.get(new PageContext("testOpenPageMasterParam"));
        assertThat(((Filter) p2.getWidgets().get("testOpenPageMasterParam_modalDetail").getFilters().get(0)).getParam(), is("sid"));
        assertThat(p2.getRoutes().getList().get(1).getPath(), is("/testOpenPageMasterParam/:testOpenPageMasterParam_form_id"));
        assertThat(p2.getRoutes().getList().get(4).getPath(), is("/testOpenPageMasterParam/detail2/:testOpenPageMasterParam_modalDetail_id"));
    }
}
