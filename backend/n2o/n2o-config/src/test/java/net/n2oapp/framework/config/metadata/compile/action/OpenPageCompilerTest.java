package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.*;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.action.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.InputSelect;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
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
import java.util.HashMap;
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
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testRefbook.query.xml"));
    }

    @Test
    public void filterModel() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .get(new PageContext("testOpenPageSimplePage"));
        PageContext context = (PageContext) route("/page/widget/action1", Page.class);
        SimplePage openPage = (SimplePage) read().compile().get(context);

        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        assertThat(openPage.getWidget().getActions().size(), is(2));
        InvokeAction submit = (InvokeAction) openPage.getWidget().getActions().get("submit");
        InvokeActionPayload submitPayload = submit.getPayload();
        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/page/widget/action1/submit"));
        assertThat(submitPayload.getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(submitPayload.getModelLink(), is("models.resolve['page_widget_action1_main']"));
        assertThat(submitPayload.getWidgetId(), is("page_widget_action1_main"));
        AsyncMetaSaga meta = submit.getMeta();
        assertThat(meta.getSuccess().getRefresh().getOptions().getWidgetId(), is("page_test"));
        assertThat(meta.getSuccess().getMessageWidgetId(), is("page_test"));
        assertThat(meta.getSuccess().getCloseLastModal(), nullValue());
        assertThat(meta.getSuccess().getRedirect().getPath(), is("/page/widget"));
        ActionContext submitContext = (ActionContext) route("/page/widget/action1/submit", CompiledObject.class);
        assertThat(submitContext.getRedirect(), nullValue());

        LinkActionImpl close = (LinkActionImpl) openPage.getWidget().getActions().get("close");
        assertThat(close.getUrl(), is("/page/widget"));
        assertThat(close.getTarget(), is(Target.application));

        PageRoutes.Route action1 = page.getRoutes().findRouteByUrl("/page/widget/action1");
        assertThat(action1.getIsOtherPage(), is(true));
    }

    @Test
    public void resolveModel() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml")
                .get(new PageContext("testOpenPageSimplePage", "/page"));
        assertThat(((LinkActionImpl) page.getWidget().getActions().get("id2")).getPathMapping().get("page_test_id").getBindLink(), is("models.resolve['page_test'].id"));
        assertThat(((LinkActionImpl) page.getWidget().getActions().get("id2")).getQueryMapping().size(), is(0));

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
        assertThat(pathMapping.get("page_widget_action2_main_id").getPayload().get("widgetId"), is("page_widget_action2_main"));
        assertThat(pathMapping.get("page_widget_action2_main_id").getPayload().get("value"), is(":page_widget_action2_main_id"));


        assertThat(openPage.getWidget().getActions().size(), is(2));
        InvokeAction submit = (InvokeAction) openPage.getWidget().getActions().get("submit");
        InvokeActionPayload submitPayload = submit.getPayload();
        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/page/widget/:page_test_id/action2/submit"));
        assertThat(submitPayload.getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(submitPayload.getModelLink(), is("models.resolve['page_widget_action2_main']"));
        assertThat(submitPayload.getWidgetId(), is("page_widget_action2_main"));
        AsyncMetaSaga meta = submit.getMeta();
        assertThat(meta.getSuccess().getRefresh().getOptions().getWidgetId(), is("page_test"));
        assertThat(meta.getSuccess().getCloseLastModal(), nullValue());
        assertThat(meta.getSuccess().getRedirect().getPath(), is("/page/widget/:page_test_id"));
        ActionContext submitContext = (ActionContext) route("/page/widget/123/action2/submit", CompiledObject.class);
        assertThat(submitContext.getRedirect(), nullValue());

        LinkActionImpl close = (LinkActionImpl) openPage.getWidget().getActions().get("close");
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
        data.put("detailId", "12");
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
        assertThat(level3Page.getBreadcrumb().get(2).getPath(), is("/page/123/view/widget/456/masterDetail?surname=:surname&name=:name&detailId=:detailId&secondName=test"));
    }

    @Test
    public void masterDetail() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .get(new PageContext("testOpenPageSimplePage", "/page"));

        LinkActionImpl linkAction = (LinkActionImpl) page.getWidget().getActions().get("masterDetail");
        assertThat(linkAction.getPathMapping().get("page_test_id").getBindLink(), is("models.resolve['page_test'].id"));
        assertThat(linkAction.getQueryMapping().get("detailId").getBindLink(), is("models.resolve['page_test']"));
        assertThat(linkAction.getQueryMapping().get("detailId").getValue(), is("`masterId`"));
        assertThat(linkAction.getQueryMapping().get("name").getBindLink(), is("models.filter['page_test']"));
        assertThat(linkAction.getQueryMapping().get("secondName").getBindLink(), nullValue());

        PageContext context = (PageContext) route("/page/widget/gender/masterDetail", Page.class);
        assertThat(context.getPreFilters().size(), is(1));
        assertThat(context.getPreFilters().get(0).getRefPageId(), is("page"));
        assertThat(context.getPreFilters().get(0).getRefWidgetId(), is("test"));
        assertThat(context.getPreFilters().get(0).getRefModel(), is(ReduxModel.RESOLVE));
        assertThat(context.getPreFilters().get(0).getParam(), is("detailId"));
        assertThat(context.getPreFilters().get(0).getFieldId(), is("detailId"));
        assertThat(context.getPreFilters().get(0).getValue(), is("{masterId}"));
        assertThat(context.getPreFilters().get(0).getType(), is(FilterType.eq));

        StandardPage openPage = (StandardPage) read().compile().get(context);
        assertThat(openPage.getId(), is("page_widget_masterDetail"));
        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        Filter filter = (Filter) openPage.getWidgets().get("page_widget_masterDetail_main").getFilters().get(0);
        assertThat(filter.getParam(), is("name"));
        assertThat(filter.getFilterId(), is("name"));
        assertThat(filter.getLink().getBindLink(), is("models.filter['page_test']"));
        assertThat(filter.getLink().getValue(), is("`name`"));

        filter = (Filter) openPage.getWidgets().get("page_widget_masterDetail_main").getFilters().get(1);
        assertThat(filter.getParam(), is("secondName"));
        assertThat(filter.getFilterId(), is("secondName"));
        assertThat(filter.getLink().getBindLink(), nullValue());
        assertThat(filter.getLink().getValue(), is("test"));

        filter = (Filter) openPage.getWidgets().get("page_widget_masterDetail_main").getFilters().get(2);
        assertThat(filter.getParam(), is("surname"));
        assertThat(filter.getFilterId(), is("surname"));
        assertThat(filter.getLink().getBindLink(), is("models.filter['page_test']"));
        assertThat(filter.getLink().getValue(), is("`surname`"));

        filter = (Filter) openPage.getWidgets().get("page_widget_masterDetail_main").getFilters().get(3);
        assertThat(filter.getParam(), is("detailId"));
        assertThat(filter.getFilterId(), is("detailId"));
        assertThat(filter.getLink().getBindLink(), is("models.resolve['page_test']"));
        assertThat(filter.getLink().getValue(), is("`masterId`"));

        Map<String, ReduxAction> pathMapping = openPage.getRoutes().getPathMapping();
        assertThat(pathMapping.size(), is(1));
        assertThat(pathMapping.get("page_widget_masterDetail_main_id").getType(), is("n2o/widgets/CHANGE_SELECTED_ID"));
        assertThat(pathMapping.get("page_widget_masterDetail_main_id").getPayload().get("widgetId"), is("page_widget_masterDetail_main"));
        assertThat(pathMapping.get("page_widget_masterDetail_main_id").getPayload().get("value"), is(":page_widget_masterDetail_main_id"));

        PageContext detailContext = (PageContext) route("/page/widget/gender/masterDetail", Page.class);
        assertThat(detailContext.getQueryRouteMapping().size(), is(4));
        DataSet data = new DataSet();
        data.put("detailId", 222);
        data.put("name", "testName");
        data.put("surname", "Ivanov");
        StandardPage detailPage = (StandardPage) read().compile().bind().get(detailContext, data);
        assertThat(detailPage.getRoutes().findRouteByUrl("/page/widget/:page_test_id/masterDetail"), notNullValue());
        assertThat(detailPage.getRoutes().findRouteByUrl("/page/widget/:page_test_id/masterDetail"), notNullValue());
        Map<String, BindLink> queryMapping = detailPage.getWidgets().get("page_widget_masterDetail_main").getDataProvider().getQueryMapping();
        assertThat(queryMapping.get("detailId").getValue(), is(222));
        assertThat(queryMapping.get("name").getValue(), is("testName"));
        assertThat(queryMapping.get("surname").getValue(), is("Ivanov"));
        filter = (Filter) detailPage.getWidgets().get("page_widget_masterDetail_main").getFilters().get(0);
        assertThat(filter.getParam(), is("name"));
        assertThat(filter.getFilterId(), is("name"));
        assertThat(filter.getLink().getValue(), is("testName"));
        filter = (Filter) detailPage.getWidgets().get("page_widget_masterDetail_main").getFilters().get(1);
        assertThat(filter.getParam(), is("secondName"));
        assertThat(filter.getFilterId(), is("secondName"));
        assertThat(filter.getLink().getValue(), is("test"));
        filter = (Filter) detailPage.getWidgets().get("page_widget_masterDetail_main").getFilters().get(2);
        assertThat(filter.getParam(), is("surname"));
        assertThat(filter.getFilterId(), is("surname"));
        assertThat(filter.getLink().getValue(), is("Ivanov"));
        filter = (Filter) detailPage.getWidgets().get("page_widget_masterDetail_main").getFilters().get(3);
        assertThat(filter.getParam(), is("detailId"));
        assertThat(filter.getFilterId(), is("detailId"));
        assertThat(filter.getLink().getValue(), is(222));
        assertThat(context.getQueryRouteMapping().get("surname").getValue(), is("`surname`"));
        assertThat(context.getQueryRouteMapping().get("surname").getBindLink(), is("models.filter['page_test']"));
    }

    @Test
    public void dynamicPage() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageDynamicPage.page.xml")
                .get(new PageContext("testOpenPageDynamicPage", "/page"));
        PageContext context = (PageContext) route("/page/widget/testOpenPageSimplePageAction1/id1", Page.class);
        DataSet data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction1");
        SimplePage openPage = (SimplePage) read().compile().bind().get(context, data);

        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second"));

        assertThat(openPage.getWidget().getId(), is("page_widget_testOpenPageSimplePageAction1_id1_main"));

        assertThat(openPage.getRoutes().getList().get(0).getPath(), is("/page/widget/testOpenPageSimplePageAction1/id1"));
        assertThat(openPage.getRoutes().getList().get(1).getPath(), is("/page/widget/testOpenPageSimplePageAction1/id1/:page_widget_testOpenPageSimplePageAction1_id1_main_id"));

        assertThat(openPage.getWidget(), instanceOf(Form.class));

        context = (PageContext) route("/page/widget/testOpenPageSimplePageAction2/id1", Page.class);
        data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction2");
        openPage = (SimplePage) read().compile().bind().get(context, data);

        assertThat(openPage.getBreadcrumb().size(), is(2));
        assertThat(openPage.getBreadcrumb().get(0).getLabel(), is("first"));
        assertThat(openPage.getBreadcrumb().get(1).getLabel(), is("second2"));

        assertThat(openPage.getWidget().getId(), is("page_widget_testOpenPageSimplePageAction2_id1_main"));

        assertThat(openPage.getRoutes().getList().get(0).getPath(), is("/page/widget/testOpenPageSimplePageAction2/id1"));
        assertThat(openPage.getRoutes().getList().get(1).getPath(), is("/page/widget/testOpenPageSimplePageAction2/id1/:page_widget_testOpenPageSimplePageAction2_id1_main_id"));

        assertThat(openPage.getWidget(), instanceOf(Form.class));
    }

    @Test
    public void testMasterParam() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = compile("net/n2oapp/framework/config/metadata/compile/action/testMasterParam.page.xml",
                "net/n2oapp/framework/config/metadata/compile/action/testOpenPageMasterParam.page.xml");

        Page p1 = pipeline.get(new PageContext("testMasterParam", "/page"));
        assertThat(p1.getRoutes().findRouteByUrl("/page"), notNullValue());
        assertThat(p1.getRoutes().findRouteByUrl("/page/master"), notNullValue());
        assertThat(p1.getRoutes().findRouteByUrl("/page/master/:page_master_id"), notNullValue());
        assertThat(p1.getRoutes().findRouteByUrl("/page/master/:sid/menuItem0"), notNullValue());
        assertThat(p1.getRoutes().findRouteByUrl("/page/master/:sid/detail"), notNullValue());
        assertThat(p1.getRoutes().findRouteByUrl("/page/master/:sid/detail/:page_detail_id"), notNullValue());
        assertThat(p1.getRoutes().findRouteByUrl("/page/master/:sid/detail/:sid/menuItem0"), notNullValue());

        StandardPage p2 = (StandardPage) pipeline.get(new PageContext("testOpenPageMasterParam"));
        assertThat(((Filter) p2.getWidgets().get("testOpenPageMasterParam_modalDetail").getFilters().get(0)).getParam(), is("sid"));
        assertThat(p2.getRoutes().findRouteByUrl("/testOpenPageMasterParam/:testOpenPageMasterParam_form_id"), notNullValue());
        assertThat(p2.getRoutes().findRouteByUrl("/testOpenPageMasterParam/detail2/:testOpenPageMasterParam_modalDetail_id"), notNullValue());

        ShowModal showModal = (ShowModal)p2.getWidgets().get("testOpenPageMasterParam_form").getActions().get("byName");
        assertThat(showModal.getPayload().getPageUrl(), is("/testOpenPageMasterParam/:id/byName"));
        Map<String, ModelLink> pathMapping = showModal.getPayload().getPathMapping();
        Map<String, ModelLink> queryMapping = showModal.getPayload().getQueryMapping();

        assertThat(pathMapping.size(), is(1));
        assertThat(queryMapping.size(), is(1));

        ModelLink pathModelLink = pathMapping.get("id");
        ModelLink queryModelLink = queryMapping.get("sName");

        assertThat(pathModelLink.getFieldId(), is("id"));
        assertThat(pathModelLink.getParam(), nullValue());
        assertThat(pathModelLink.getValue(), nullValue());
        assertThat(pathModelLink.getBindLink(), is("models.resolve['testOpenPageMasterParam_form'].id"));

        assertThat(queryModelLink.getFieldId(), is("name"));
        assertThat(queryModelLink.getParam(), nullValue());
        assertThat(queryModelLink.getValue(), is("`name`"));
        assertThat(queryModelLink.getBindLink(), is("models.resolve['testOpenPageMasterParam_form']"));
    }

    @Test
    public void testDefaultParam() {
        compile("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePage.page.xml")
                .get(new PageContext("testOpenPageSimplePage", "/page"));
        PageContext context = (PageContext) route("/page/widget/defaultValue", Page.class);
        SimplePage openPage = (SimplePage) read().compile().get(context);
        Map<String, PageRoutes.Query> queryMapping = openPage.getRoutes().getQueryMapping();
        assertThat(queryMapping.size(), is(2));
        ReduxAction onGet = queryMapping.get("name").getOnGet();
        assertThat(onGet.getPayload().get("prefix"), is("resolve"));
        assertThat(onGet.getPayload().get("key"), is("page_widget_defaultValue_main"));
        assertThat(onGet.getPayload().get("field"), is("surname"));
        assertThat(onGet.getPayload().get("value"), is(":name"));
        assertThat(queryMapping.get("name").getOnSet().getBindLink(), is("models.resolve['page_widget_defaultValue_main'].surname"));
        onGet = queryMapping.get("gender_id").getOnGet();
        assertThat(onGet.getPayload().get("prefix"), is("resolve"));
        assertThat(onGet.getPayload().get("key"), is("page_widget_defaultValue_main"));
        assertThat(onGet.getPayload().get("field"), is("gender.id"));
        assertThat(onGet.getPayload().get("value"), is(":gender_id"));
        assertThat(queryMapping.get("gender_id").getOnSet().getBindLink(), is("models.resolve['page_widget_defaultValue_main'].gender.id"));

        DataSet data = new DataSet();
        data.put("detailId", 222);
        data.put("name", "testName");
        data.put("surname", "Ivanov");
        openPage = (SimplePage) read().compile().bind().get(context, data);
        assertThat(openPage.getModels().size(), is(0));

        context = (PageContext) route("/page/widget/defaultValueQuery", Page.class);
        openPage = (SimplePage) read().compile().get(context);
        queryMapping = openPage.getRoutes().getQueryMapping();
        assertThat(queryMapping.size(), is(0));

        context = (PageContext) route("/page/widget/testPreFilter", Page.class);
        openPage = (SimplePage) read().compile().get(context);
        Map<String, BindLink> queryMapping1 = ((InputSelect) ((StandardField) ((Form) openPage.getWidget()).getComponent().getFieldsets().get(0).getRows().get(0)
                .getCols().get(0).getFields().get(0)).getControl()).getDataProvider().getQueryMapping();
        assertThat(queryMapping1.size(), is(1));
        assertThat(queryMapping1.get("id").getValue(), is(1));
    }
}
