package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.datasource.Datasource;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
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
        OpenDrawerPayload payload = ((OpenDrawer) table.getToolbar().getButton("create").getAction()).getPayload();
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

        assertThat(drawerPage.getToolbar().getButton("submit"), notNullValue());
        assertThat(drawerPage.getToolbar().getButton("close"), notNullValue());

        assertThat(drawerPage.getDatasources().get(drawerPage.getWidget().getId()).getDefaultValuesMode(), is(DefaultValuesMode.defaults));

        List<AbstractButton> buttons = drawerPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.size(), is(2));
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), notNullValue());
        assertThat(buttons.get(1).getId(), is("close"));
        assertThat(buttons.get(1).getAction(), notNullValue());

        InvokeAction submit = (InvokeAction) drawerPage.getToolbar().getButton("submit").getAction();
        InvokeActionPayload submitPayload = submit.getPayload();
        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/p/create/submit"));
        assertThat(submitPayload.getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(submitPayload.getModel(), is(ReduxModel.edit));
        assertThat(submitPayload.getDatasource(), is("p_create_main"));
        AsyncMetaSaga meta = submit.getMeta();
        assertThat(meta.getSuccess().getRefresh().getDatasources(), hasItem("p_second"));
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
        OpenDrawerPayload payload = ((OpenDrawer) table.getToolbar().getButton("update").getAction()).getPayload();

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
        assertThat(drawerContext.getPreFilters().get(0).getModel(), is(ReduxModel.resolve));
        assertThat(drawerContext.getPreFilters().get(0).getValue(), is("{secondId}"));
        assertThat(drawerContext.getUpload(), is(UploadType.query));
        SimplePage drawerPage = (SimplePage) read().compile().get(drawerContext);
        assertThat(drawerPage.getId(), is("p_update"));
        assertThat(drawerPage.getBreadcrumb(), nullValue());
        Datasource datasource = drawerPage.getDatasources().get(drawerPage.getWidget().getId());
        assertThat(datasource.getProvider().getUrl(), is("n2o/data/p/:id/update/main"));
        assertThat(datasource.getProvider().getQueryMapping().size(), is(0));
        assertThat(datasource.getProvider().getPathMapping().get("id").getBindLink(), is("models.resolve['p_main']"));
        assertThat(datasource.getProvider().getPathMapping().get("id").getValue(), is("`id`"));
        assertThat(datasource.getDefaultValuesMode(), is(DefaultValuesMode.query));

        DataSet data = new DataSet();
        data.put("id", 222);
        drawerPage = (SimplePage) read().compile().bind().get(drawerContext, data);
        OpenDrawer openDrawer = (OpenDrawer) drawerPage.getWidget().getToolbar().getButton("mi0").getAction();
        assertThat(openDrawer.getPayload().getPageUrl(), is("/p/222/update/mi0"));
        assertThat(drawerPage.getDatasources().get(drawerPage.getWidget().getId()).getProvider().getUrl(), is("n2o/data/p/222/update/main"));
    }

    @Test
    public void createFocus() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);
        SimplePage openDrawer = (SimplePage) routeAndGet("/p/createFocus", Page.class);
        InvokeAction submit = (InvokeAction) openDrawer.getToolbar().getButton("submit").getAction();
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), notNullValue());
        assertThat(submit.getMeta().getSuccess().getRefresh().getDatasources(), hasItem("p_main"));

        CloseAction close = (CloseAction) openDrawer.getToolbar().getButton("close").getAction();
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
    }

    @Test
    public void updateFocus() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);
        StandardPage openDrawer = (StandardPage) routeAndGet("/p/123/updateFocus", Page.class);
        InvokeAction submit = (InvokeAction) openDrawer.getToolbar().getButton("submit").getAction();
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), notNullValue());
        assertThat(submit.getMeta().getSuccess().getRefresh().getDatasources(), hasItem("p_main"));

        CloseAction close = (CloseAction) openDrawer.getToolbar().getButton("close").getAction();
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
        Widget modalWidget = (Widget) openDrawer.getRegions().get("left").get(0).getContent().get(0);
        assertThat(openDrawer.getDatasources().get(modalWidget.getDatasource()).getProvider().getPathMapping().size(), is(0));
        assertThat(openDrawer.getDatasources().get(modalWidget.getDatasource()).getProvider().getQueryMapping().size(), is(0));
    }

    @Test
    public void createUpdate() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);
        SimplePage openDrawer = (SimplePage) routeAndGet("/p/createUpdate", Page.class);
        InvokeAction submit = (InvokeAction) openDrawer.getToolbar().getButton("submit").getAction();
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), notNullValue());
        assertThat(submit.getMeta().getSuccess().getRedirect().getPath(), is("/p/:id/update"));
        //Есть обновление, потому что по умолчанию true. Обновится родительский виджет, потому что close-after-submit=true
        assertThat(submit.getMeta().getSuccess().getRefresh().getDatasources(), hasItem("p_main"));
        //Есть уведомление, потому что по умолчанию true. Уведомление будет на родительском виджете, потому что close-after-submit=true

        CloseAction close = (CloseAction) openDrawer.getToolbar().getButton("close").getAction();
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
    }

    @Test
    public void dynamicPage() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerDynamicPage.page.xml")
                .get(new PageContext("testOpenDrawerDynamicPage", "/page"));
        PageContext context = (PageContext) route("/page/testOpenPageSimplePageAction1/id1", Page.class);
        DataSet data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction1");
        SimplePage openDrawer = (SimplePage) read().compile().bind().get(context, data);
        assertThat(openDrawer.getId(), is("page_id1"));
        assertThat(openDrawer.getWidget(), instanceOf(Form.class));

        context = (PageContext) route("/page/testOpenPageSimplePageAction2/id1", Page.class);
        data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction2");
        openDrawer = (SimplePage) read().compile().bind().get(context, data);
        assertThat(openDrawer.getId(), is("page_id1"));
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
        assertThat(drawerContext.getPreFilters().get(0).getModel(), is(ReduxModel.resolve));
        assertThat(drawerContext.getPreFilters().get(0).getValue(), is("{id}"));
        assertThat(drawerContext.getUpload(), is(UploadType.query));

        SimplePage drawerPage = (SimplePage) read().compile().get(drawerContext);
        assertThat(drawerPage.getId(), is("p_updateWithPrefilters"));
        assertThat(drawerPage.getBreadcrumb(), nullValue());
        Datasource datasource = drawerPage.getDatasources().get(drawerPage.getWidget().getId());
        assertThat(datasource.getProvider().getUrl(), is("n2o/data/p/:id/updateWithPrefilters/main"));
        assertThat(datasource.getProvider().getPathMapping().get("id").getBindLink(), is("models.resolve['p_main']"));
        assertThat(datasource.getProvider().getPathMapping().get("id").getValue(), is("`id`"));
        assertThat(datasource.getProvider().getQueryMapping().get("name").getBindLink(), is("models.filter['p_second']"));
        assertThat(datasource.getProvider().getQueryMapping().get("name").getValue(), is("`name`"));
        assertThat(datasource.getProvider().getQueryMapping().get("main_secondId").getParam(), is("main_secondId"));
        assertThat(datasource.getProvider().getQueryMapping().get("main_secondId").getValue(), is(1));
        List<AbstractButton> buttons = drawerPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.size(), is(2));
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), notNullValue());
//        assertThat(buttons.get(0).getLabel(), is("Сохранить"));
        assertThat(buttons.get(1).getId(), is("close"));
        assertThat(buttons.get(1).getAction(), notNullValue());
//        assertThat(buttons.get(1).getLabel(), is("Закрыть"));
        InvokeAction submit = (InvokeAction) drawerPage.getToolbar().getButton("submit").getAction();
        assertThat(submit.getMeta().getSuccess().getRefresh().getDatasources(), hasItem("p_main"));
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), notNullValue());
        assertThat(submit.getPayload().getDataProvider().getUrl(), is("n2o/data/p/:id/updateWithPrefilters/submit"));
        ActionContext submitContext = (ActionContext) route("/p/:id/updateWithPrefilters/submit", CompiledObject.class);
        assertThat(submitContext.getSourceId(null), is("testShowModal"));
        assertThat(submitContext.getOperationId(), is("update"));
        assertThat(submitContext.getOperationId(), is("update"));

        DataSet data = new DataSet();
        data.put("id", 222);
        drawerPage = (SimplePage) read().compile().bind().get(drawerContext, data);
        assertThat(drawerPage.getDatasources().get(drawerPage.getWidget().getId()).getProvider().getUrl(), is("n2o/data/p/222/updateWithPrefilters/main"));
        submit = (InvokeAction) drawerPage.getToolbar().getButton("submit").getAction();
        assertThat(submit.getPayload().getDataProvider().getPathMapping(), not(hasKey("id")));
    }

    @Test
    public void updateModelEditWithPreFilters() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);
        OpenDrawer openDrawer = (OpenDrawer) ((Widget) rootPage.getRegions().get("left").get(0).getContent().get(0))
                .getToolbar().getButton("updateEditWithPrefilters").getAction();
        assertThat(openDrawer.getPayload().getQueryMapping().get("id").getBindLink(), is("models.edit['p_main']"));

        Page openDrawerPage = routeAndGet("/p/updateEditWithPrefilters", Page.class);
        assertThat(openDrawerPage.getId(), is("p_updateEditWithPrefilters"));
    }
}