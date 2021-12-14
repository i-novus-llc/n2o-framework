package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.modal.open_drawer.OpenDrawer;
import net.n2oapp.framework.api.metadata.meta.action.modal.open_drawer.OpenDrawerPayload;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.saga.AsyncMetaSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OpenDrawerCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack(), new N2oAllDataPack(),
                new N2oActionsPack(), new N2oCellsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerPageSecondFlow.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage2.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage3.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageDynamicPage.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction1.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction2.page.xml"));
    }

    @Test
    public void create() {
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(new PageContext("testOpenDrawerRootPage", "/p"));


        Table table = (Table) rootPage.getRegions().get("left").get(0).getContent().get(0);
        OpenDrawerPayload payload = ((OpenDrawer) table.getActions().get("create")).getPayload();
        //create
        assertThat(payload.getPageUrl(), is("/p/create"));
        assertThat(payload.getPageId(), is("p_create"));
        assertThat(payload.getMode(), is("drawer"));
        assertThat(payload.getPrompt(), is(true));
        assertThat(payload.getFixedFooter(), is(false));
        assertThat(payload.getCloseOnEscape(), is(true));

        PageContext drawerContext = (PageContext) route("/p/create", Page.class);
        assertThat(drawerContext.getSourceId(null), is("testShowModalPage"));
        assertThat(drawerContext.getUpload(), is(UploadType.defaults));
        SimplePage drawerPage = (SimplePage) read().compile().get(drawerContext);
        assertThat(drawerPage.getId(), is("p_create"));
        assertThat(drawerPage.getBreadcrumb(), nullValue());

        assertThat(drawerPage.getWidget().getActions().size(), is(2));
        assertThat(drawerPage.getWidget().getActions().containsKey("submit"), is(true));
        assertThat(drawerPage.getWidget().getActions().containsKey("close"), is(true));

        Widget drawerWidget = drawerPage.getWidget();
        assertThat(drawerWidget.getUpload(), is(UploadType.defaults));

        List<AbstractButton> buttons = drawerPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.size(), is(2));
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), notNullValue());
        assertThat(buttons.get(1).getId(), is("close"));
        assertThat(buttons.get(1).getAction(), notNullValue());

        InvokeAction submit = (InvokeAction) drawerPage.getWidget().getActions().get("submit");
        InvokeActionPayload submitPayload = submit.getPayload();
        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/p/create/submit"));
        assertThat(submitPayload.getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(submitPayload.getModelLink(), is("models.edit['p_create_main']"));
        assertThat(submitPayload.getWidgetId(), is("p_create_main"));
        AsyncMetaSaga meta = submit.getMeta();
        assertThat(meta.getSuccess().getRefresh().getOptions().getWidgetId(), is("p_second"));
        assertThat(meta.getSuccess().getModalsToClose(), notNullValue());
        assertThat(meta.getFail().getMessageWidgetId(), is("p_create_main"));
        assertThat(meta.getSuccess().getMessageWidgetId(), is("p_main"));
        assertThat(submit.getPayload().getDataProvider().getUrl(), is("n2o/data/p/create/submit"));

        ActionContext submitContext = (ActionContext) route("/p/create/submit", CompiledObject.class);
        assertThat(submitContext.getSourceId(null), is("testShowModal"));
        assertThat(submitContext.getOperationId(), is("create"));
    }

    @Test
    public void update() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);

        Table table = (Table) rootPage.getRegions().get("left").get(0).getContent().get(0);
        OpenDrawerPayload payload = ((OpenDrawer) table.getActions().get("update")).getPayload();

        //update
        assertThat(payload.getPageUrl(), is("/p/:id/update"));
        assertThat(payload.getPrompt(), is(false));
        assertThat(payload.getFixedFooter(), is(true));

        PageContext drawerContext = (PageContext) route("/p/123/update", Page.class);
        assertThat(drawerContext.getSourceId(null), is("testOpenDrawerPageSecondFlow"));
        assertThat(drawerContext.getPreFilters().size(), is(1));
        assertThat(drawerContext.getPreFilters().get(0).getRefWidgetId(), is("main"));
        assertThat(drawerContext.getPreFilters().get(0).getRefPageId(), is("p"));
        assertThat(drawerContext.getPreFilters().get(0).getFieldId(), is(N2oQuery.Field.PK));
        assertThat(drawerContext.getPreFilters().get(0).getType(), is(FilterType.eq));
        assertThat(drawerContext.getPreFilters().get(0).getRefModel(), is(ReduxModel.RESOLVE));
        assertThat(drawerContext.getPreFilters().get(0).getValue(), is("{secondId}"));
        assertThat(drawerContext.getUpload(), is(UploadType.query));
        SimplePage drawerPage = (SimplePage) read().compile().get(drawerContext);
        assertThat(drawerPage.getId(), is("p_update"));
        assertThat(drawerPage.getBreadcrumb(), nullValue());
        Widget drawerWidget = drawerPage.getWidget();
        List<Filter> filters = drawerWidget.getFilters();
        assertThat(filters.get(0).getParam(), is("id"));
        assertThat(filters.get(0).getFilterId(), is("id"));
        assertThat(filters.get(0).getRoutable(), is(false));
        assertThat(filters.get(0).getLink().getBindLink(), is("models.resolve['p_main']"));
        assertThat(filters.get(0).getLink().getValue(), is("`secondId`"));
        assertThat(drawerWidget.getDataProvider().getQueryMapping().size(), is(0));
        assertThat(drawerWidget.getDataProvider().getPathMapping().get("id").getBindLink(), is("models.resolve['p_main']"));
        assertThat(drawerWidget.getDataProvider().getPathMapping().get("id").getValue(), is("`id`"));
        assertThat(drawerWidget.getUpload(), is(UploadType.query));

        DataSet data = new DataSet();
        data.put("id", 222);
        drawerPage = (SimplePage) read().compile().bind().get(drawerContext, data);
        OpenDrawer openDrawer = (OpenDrawer) drawerPage.getWidget().getActions().get("menuItem0");
        assertThat(openDrawer.getPayload().getPageUrl(), is("/p/222/update/menuItem0"));
        assertThat(drawerPage.getWidget().getDataProvider().getUrl(), is("n2o/data/p/222/update"));

        QueryContext queryContext = (QueryContext) route("/p/123/update", CompiledQuery.class);
        assertThat(queryContext.getValidations().size(), is(1));
    }

    @Test
    public void createFocus() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);
        SimplePage openDrawer = (SimplePage) routeAndGet("/p/createFocus", Page.class);
        InvokeAction submit = (InvokeAction) openDrawer.getWidget().getActions().get("submit");
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), notNullValue());
        assertThat(submit.getMeta().getSuccess().getRedirect().getPath(), is("/p/:id"));
        assertThat(submit.getMeta().getSuccess().getRefresh().getOptions().getWidgetId(), is("p_main"));

        CloseAction close = (CloseAction) openDrawer.getWidget().getActions().get("close");
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
    }

    @Test
    public void updateFocus() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);
        StandardPage openDrawer = (StandardPage) routeAndGet("/p/123/updateFocus", Page.class);
        InvokeAction submit = (InvokeAction) openDrawer.getActions().get("submit");
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), notNullValue());
        assertThat(submit.getMeta().getSuccess().getRedirect().getPath(), is("/p/:id"));
        assertThat(submit.getMeta().getSuccess().getRefresh().getOptions().getWidgetId(), is("p_main"));

        CloseAction close = (CloseAction) openDrawer.getActions().get("close");
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
        Widget modalWidget = (Widget) openDrawer.getRegions().get("left").get(0).getContent().get(0);
        assertThat(modalWidget.getDataProvider().getPathMapping().size(), is(0));
        assertThat(modalWidget.getDataProvider().getQueryMapping().size(), is(0));
    }

    @Test
    public void createUpdate() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);
        SimplePage openDrawer = (SimplePage) routeAndGet("/p/createUpdate", Page.class);
        InvokeAction submit = (InvokeAction) openDrawer.getWidget().getActions().get("submit");
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), notNullValue());
        assertThat(submit.getMeta().getSuccess().getRedirect().getPath(), is("/p/:id/update"));
        //Есть обновление, потому что по умолчанию true. Обновится родительский виджет, потому что close-after-submit=true
        assertThat(submit.getMeta().getSuccess().getRefresh().getOptions().getWidgetId(), is("p_main"));
        //Есть уведомление, потому что по умолчанию true. Уведомление будет на родительском виджете, потому что close-after-submit=true

        CloseAction close = (CloseAction) openDrawer.getWidget().getActions().get("close");
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
    }

    @Test
    public void dynamicPage() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerDynamicPage.page.xml")
                .get(new PageContext("testOpenDrawerDynamicPage", "/page"));
        PageContext context = (PageContext) route("/page/widget/testOpenPageSimplePageAction1/id1", Page.class);
        DataSet data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction1");
        SimplePage openDrawer = (SimplePage) read().compile().bind().get(context, data);
        assertThat(openDrawer.getId(), is("page_widget_id1"));
        assertThat(openDrawer.getWidget(), instanceOf(Form.class));

        context = (PageContext) route("/page/widget/testOpenPageSimplePageAction2/id1", Page.class);
        data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction2");
        openDrawer = (SimplePage) read().compile().bind().get(context, data);
        assertThat(openDrawer.getId(), is("page_widget_id1"));
        assertThat(openDrawer.getWidget(), instanceOf(Form.class));
    }

    @Test
    public void updateWithPreFilters() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);

        PageContext drawerContext = (PageContext) route("/p/123/updateWithPrefilters", Page.class);
        assertThat(drawerContext.getSourceId(null), is("testShowModalPage"));
        assertThat(drawerContext.getPreFilters().size(), is(1));
        assertThat(drawerContext.getPreFilters().get(0).getRefWidgetId(), is("main"));
        assertThat(drawerContext.getPreFilters().get(0).getRefPageId(), is("p"));
        assertThat(drawerContext.getPreFilters().get(0).getFieldId(), is(N2oQuery.Field.PK));
        assertThat(drawerContext.getPreFilters().get(0).getType(), is(FilterType.eq));
        assertThat(drawerContext.getPreFilters().get(0).getRefModel(), is(ReduxModel.RESOLVE));
        assertThat(drawerContext.getPreFilters().get(0).getValue(), is("{id}"));
        assertThat(drawerContext.getUpload(), is(UploadType.query));

        SimplePage drawerPage = (SimplePage) read().compile().get(drawerContext);
        assertThat(drawerPage.getId(), is("p_updateWithPrefilters"));
        assertThat(drawerPage.getBreadcrumb(), nullValue());
        Widget drawerWidget = drawerPage.getWidget();
        List<Filter> filters = drawerWidget.getFilters();
        assertThat(filters.get(2).getParam(), is("id"));
        assertThat(filters.get(2).getFilterId(), is("id"));
        assertThat(filters.get(2).getRoutable(), is(false));
        assertThat(filters.get(2).getLink().getBindLink(), is("models.resolve['p_main']"));
        assertThat(filters.get(2).getLink().getValue(), is("`id`"));
        assertThat(filters.get(0).getParam(), is("p_updateWithPrefilters_main_secondId"));
        assertThat(filters.get(0).getFilterId(), is("secondId"));
        assertThat(filters.get(0).getRoutable(), is(false));
        assertThat(filters.get(0).getLink().getBindLink(), nullValue());
        assertThat(filters.get(0).getLink().getValue(), is(1));
        assertThat(filters.get(1).getParam(), is("name"));
        assertThat(filters.get(1).getFilterId(), is("name"));
        assertThat(filters.get(1).getRoutable(), is(false));
        assertThat(filters.get(1).getLink().getBindLink(), is("models.filter['p_second']"));
        assertThat(filters.get(1).getLink().getValue(), is("`name`"));

        assertThat(drawerWidget.getDataProvider().getPathMapping().get("id").getBindLink(), is("models.resolve['p_main']"));
        assertThat(drawerWidget.getDataProvider().getPathMapping().get("id").getValue(), is("`id`"));
        assertThat(drawerWidget.getDataProvider().getQueryMapping().get("name").getBindLink(), is("models.filter['p_second']"));
        assertThat(drawerWidget.getDataProvider().getQueryMapping().get("name").getValue(), is("`name`"));

        assertThat(drawerWidget.getUpload(), is(UploadType.query));
        List<AbstractButton> buttons = drawerPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.size(), is(2));
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), notNullValue());
//        assertThat(buttons.get(0).getLabel(), is("Сохранить"));
        assertThat(buttons.get(1).getId(), is("close"));
        assertThat(buttons.get(1).getAction(), notNullValue());
//        assertThat(buttons.get(1).getLabel(), is("Закрыть"));
        InvokeAction submit = (InvokeAction) drawerPage.getWidget().getActions().get("submit");
        assertThat(submit.getMeta().getSuccess().getRefresh().getOptions().getWidgetId(), is("p_main"));
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), notNullValue());
        assertThat(submit.getPayload().getDataProvider().getUrl(), is("n2o/data/p/:id/updateWithPrefilters/submit"));
        ActionContext submitContext = (ActionContext) route("/p/:id/updateWithPrefilters/submit", CompiledObject.class);
        assertThat(submitContext.getSourceId(null), is("testShowModal"));
        assertThat(submitContext.getOperationId(), is("update"));
        assertThat(submitContext.getOperationId(), is("update"));

        DataSet data = new DataSet();
        data.put("id", 222);
        drawerPage = (SimplePage) read().compile().bind().get(drawerContext, data);
        assertThat(drawerPage.getWidget().getDataProvider().getUrl(), is("n2o/data/p/222/updateWithPrefilters"));
        submit = (InvokeAction) drawerPage.getToolbar().getButton("submit").getAction();
        assertThat(submit.getPayload().getDataProvider().getPathMapping(), not(hasKey("id")));
    }

    @Test
    public void updateModelEditWithPreFilters() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);
        OpenDrawer openDrawer = (OpenDrawer) ((Widget) rootPage.getRegions().get("left").get(0).getContent().get(0))
                .getActions().get("updateEditWithPrefilters");
        assertThat(openDrawer.getPayload().getQueryMapping().get("id").getBindLink(), is("models.edit['p_main']"));

        Page openDrawerPage = routeAndGet("/p/updateEditWithPrefilters", Page.class);
        assertThat(openDrawerPage.getId(), is("p_updateEditWithPrefilters"));
    }
}