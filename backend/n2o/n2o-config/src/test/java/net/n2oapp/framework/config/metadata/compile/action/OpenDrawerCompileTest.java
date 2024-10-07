package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseAction;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.modal.open_drawer.OpenDrawer;
import net.n2oapp.framework.api.metadata.meta.action.modal.open_drawer.OpenDrawerPayload;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshAction;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshPayload;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OpenDrawerCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack(), new N2oAllDataPack(),
                new N2oActionsPack(), new N2oCellsPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerPageSecondFlow.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage2.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage3.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageDynamicPage.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction1.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction2.page.xml"));
    }

    @Test
    void create() {
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(new PageContext("testOpenDrawerRootPage", "/p"));


        Table table = (Table) rootPage.getRegions().get("single").get(0).getContent().get(0);
        OpenDrawerPayload payload = ((OpenDrawer) table.getToolbar().getButton("create").getAction()).getPayload();
        //create
        assertThat(payload.getPageUrl(), is("/p/create"));
        assertThat(payload.getPageId(), is("p_create"));
        assertThat(payload.getMode(), is("drawer"));
        assertThat(payload.getPrompt(), is(false));
        assertThat(payload.getFixedFooter(), is(false));
        assertThat(payload.getCloseOnEscape(), is(true));
        assertThat(payload.getWidth(), is("200px"));
        assertThat(payload.getHeight(), is("300px"));

        PageContext drawerContext = (PageContext) route("/p/create", Page.class);
        assertThat(drawerContext.getSourceId(null), is("testShowModalPage"));
        assertThat(drawerContext.getDatasources().get(0).getId(),
                is("parent_second"));
        assertThat(((N2oStandardDatasource) drawerContext.getDatasources().get(1)).getDefaultValuesMode(),
                is(DefaultValuesMode.defaults));
        SimplePage drawerPage = (SimplePage) read().compile().get(drawerContext);
        assertThat(drawerPage.getId(), is("p_create"));
        assertThat(drawerPage.getBreadcrumb(), nullValue());

        assertThat(drawerPage.getToolbar().getButton("submit"), notNullValue());
        assertThat(drawerPage.getToolbar().getButton("close"), notNullValue());

        assertThat(((StandardDatasource) drawerPage.getDatasources().get(drawerPage.getWidget().getId())).getDefaultValuesMode(),
                is(DefaultValuesMode.defaults));

        List<AbstractButton> buttons = drawerPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.size(), is(2));
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), notNullValue());
        assertThat(buttons.get(1).getId(), is("close"));
        assertThat(buttons.get(1).getAction(), notNullValue());

        MultiAction multiAction = (MultiAction) drawerPage.getToolbar().getButton("submit").getAction();
        InvokeAction submit = (InvokeAction) multiAction.getPayload().getActions().get(0);
        InvokeActionPayload submitPayload = submit.getPayload();
        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/p/create/multi1"));
        assertThat(submitPayload.getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(submitPayload.getModel(), is(ReduxModel.edit));
        assertThat(submitPayload.getDatasource(), is("p_create_modal"));
        AsyncMetaSaga meta = submit.getMeta();
        assertThat(meta.getSuccess().getRefresh(), nullValue());
        assertThat(meta.getSuccess().getModalsToClose(), nullValue());
        assertThat(meta.getFail().getMessageWidgetId(), is("p_create_modal"));
        assertThat(meta.getSuccess().getMessageWidgetId(), is("p_create_modal"));
        assertThat(submit.getPayload().getDataProvider().getUrl(), is("n2o/data/p/create/multi1"));
        RefreshAction refresh = (RefreshAction) multiAction.getPayload().getActions().get(1);
        assertThat(((RefreshPayload) refresh.getPayload()).getDatasource(), is("p_second"));
        CloseAction close = (CloseAction) multiAction.getPayload().getActions().get(2);
        assertThat(((CloseActionPayload) close.getPayload()).getPageId(), is("p_create"));

        ActionContext submitContext = (ActionContext) route("/p/create/multi1", CompiledObject.class);
        assertThat(submitContext.getSourceId(null), is("testShowModal"));
        assertThat(submitContext.getOperationId(), is("create"));
    }

    @Test
    void update() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);

        Table table = (Table) rootPage.getRegions().get("single").get(0).getContent().get(0);
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
        assertThat(drawerContext.getPreFilters().get(0).getFieldId(), is(QuerySimpleField.PK));
        assertThat(drawerContext.getPreFilters().get(0).getType(), is(FilterType.eq));
        assertThat(drawerContext.getPreFilters().get(0).getModel(), is(ReduxModel.resolve));
        assertThat(drawerContext.getDatasources().get(0).getId(),
                is("parent_main"));
        assertThat(((N2oStandardDatasource) drawerContext.getDatasources().get(1)).getDefaultValuesMode(),
                is(DefaultValuesMode.query));
        SimplePage drawerPage = (SimplePage) read().compile().get(drawerContext);
        assertThat(drawerPage.getId(), is("p_update"));
        assertThat(drawerPage.getBreadcrumb(), nullValue());
        StandardDatasource datasource = (StandardDatasource) drawerPage.getDatasources().get(drawerPage.getWidget().getId());
        assertThat(datasource.getProvider().getUrl(), is("n2o/data/p/:id/update/w1"));
        assertThat(datasource.getProvider().getQueryMapping().size(), is(0));
        assertThat(datasource.getProvider().getPathMapping().get("id").getBindLink(), is("models.resolve['p_main']"));
        assertThat(datasource.getProvider().getPathMapping().get("id").getValue(), is("`id`"));
        assertThat(datasource.getDefaultValuesMode(), is(DefaultValuesMode.query));

        DataSet data = new DataSet();
        data.put("id", 222);
        drawerPage = (SimplePage) read().compile().bind().get(drawerContext, data);
        OpenDrawer openDrawer = (OpenDrawer) drawerPage.getWidget().getToolbar().getButton("p_update_mi0").getAction();
        assertThat(openDrawer.getPayload().getPageUrl(), is("/p/222/update/p_update_mi0"));
        assertThat(((StandardDatasource) drawerPage.getDatasources().get(drawerPage.getWidget().getId())).getProvider().getUrl(), is("n2o/data/p/222/update/w1"));
    }

    @Test
    void createFocus() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);
        SimplePage openDrawer = (SimplePage) routeAndGet("/p/createFocus", Page.class);
        MultiAction multiAction = (MultiAction) openDrawer.getToolbar().getButton("submit").getAction();
        InvokeAction submit = (InvokeAction) multiAction.getPayload().getActions().get(0);
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), nullValue());
        assertThat(submit.getMeta().getSuccess().getRefresh(), nullValue());
        RefreshAction refresh = (RefreshAction) multiAction.getPayload().getActions().get(1);
        assertThat(((RefreshPayload) refresh.getPayload()).getDatasource(), is("p_main"));
        CloseAction close = (CloseAction) multiAction.getPayload().getActions().get(2);
        assertThat(((CloseActionPayload) close.getPayload()).getPageId(), is("p_createFocus"));

        close = (CloseAction) openDrawer.getToolbar().getButton("close").getAction();
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
    }

    @Test
    void updateFocus() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);
        StandardPage openDrawer = (StandardPage) routeAndGet("/p/123/updateFocus", Page.class);
        MultiAction multiAction = (MultiAction) openDrawer.getToolbar().getButton("submit").getAction();
        InvokeAction submit = (InvokeAction) multiAction.getPayload().getActions().get(0);
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), nullValue());
        assertThat(submit.getMeta().getSuccess().getRefresh(), nullValue());
        RefreshAction refresh = (RefreshAction) multiAction.getPayload().getActions().get(1);
        assertThat(((RefreshPayload) refresh.getPayload()).getDatasource(), is("p_main"));
        CloseAction close = (CloseAction) multiAction.getPayload().getActions().get(2);
        assertThat(((CloseActionPayload) close.getPayload()).getPageId(), is("p_updateFocus"));

        close = (CloseAction) openDrawer.getToolbar().getButton("close").getAction();
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
        Widget modalWidget = (Widget) openDrawer.getRegions().get("single").get(0).getContent().get(0);
        assertThat(((StandardDatasource) openDrawer.getDatasources().get(modalWidget.getDatasource())).getProvider().getPathMapping().size(), is(0));
        assertThat(((StandardDatasource) openDrawer.getDatasources().get(modalWidget.getDatasource())).getProvider().getQueryMapping().size(), is(0));
    }

    @Test
    void createUpdate() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);
        SimplePage openDrawer = (SimplePage) routeAndGet("/p/createUpdate", Page.class);
        InvokeAction submit = (InvokeAction) ((MultiAction) openDrawer.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(0);
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), notNullValue());
        assertThat(submit.getMeta().getSuccess().getRedirect().getPath(), is("/p/:id/update"));
        //Есть обновление, потому что по умолчанию true. Обновится родительский виджет, потому что close-after-submit=true
        RefreshAction refresh = (RefreshAction) ((MultiAction) openDrawer.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(1);
        assertThat(((RefreshPayload) refresh.getPayload()).getDatasource(), is("p_main"));
        //Есть уведомление, потому что по умолчанию true. Уведомление будет на родительском виджете, потому что close-after-submit=true
        CloseAction close = (CloseAction) ((MultiAction) openDrawer.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(2);
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
    }

    @Test
    void dynamicPage() {
        compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerDynamicPage.page.xml")
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
    void updateWithPreFilters() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);

        PageContext drawerContext = (PageContext) route("/p/123/updateWithPrefilters", Page.class);
        assertThat(drawerContext.getSourceId(null), is("testShowModalPage"));
        assertThat(drawerContext.getPreFilters().size(), is(1));
        assertThat(drawerContext.getPreFilters().get(0).getRefWidgetId(), is("main"));
        assertThat(drawerContext.getPreFilters().get(0).getRefPageId(), is("p"));
        assertThat(drawerContext.getPreFilters().get(0).getFieldId(), is(QuerySimpleField.PK));
        assertThat(drawerContext.getPreFilters().get(0).getType(), is(FilterType.eq));
        assertThat(drawerContext.getPreFilters().get(0).getModel(), is(ReduxModel.resolve));
        assertThat(drawerContext.getPreFilters().get(0).getValue(), is("{id}"));
        assertThat(drawerContext.getDatasources().get(0).getId(),
                is("parent_main"));
        assertThat(((N2oStandardDatasource) drawerContext.getDatasources().get(1)).getDefaultValuesMode(),
                is(DefaultValuesMode.query));

        SimplePage drawerPage = (SimplePage) read().compile().get(drawerContext);
        assertThat(drawerPage.getId(), is("p_updateWithPrefilters"));
        assertThat(drawerPage.getBreadcrumb(), nullValue());
        StandardDatasource datasource = (StandardDatasource) drawerPage.getDatasources().get(drawerPage.getWidget().getId());
        assertThat(datasource.getProvider().getUrl(), is("n2o/data/p/:id/updateWithPrefilters/modal"));
        assertThat(datasource.getProvider().getPathMapping().get("id").getBindLink(), is("models.resolve['p_main']"));
        assertThat(datasource.getProvider().getPathMapping().get("id").getValue(), is("`id`"));
        assertThat(datasource.getProvider().getQueryMapping().get("name").getBindLink(), is("models.filter['p_second']"));
        assertThat(datasource.getProvider().getQueryMapping().get("name").getValue(), is("`name`"));
        assertThat(datasource.getProvider().getQueryMapping().get("modal_secondId").getParam(), is("modal_secondId"));
        assertThat(datasource.getProvider().getQueryMapping().get("modal_secondId").getValue(), is(1));
        List<AbstractButton> buttons = drawerPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.size(), is(2));
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), notNullValue());
//        assertThat(buttons.get(0).getLabel(), is("Сохранить"));
        assertThat(buttons.get(1).getId(), is("close"));
        assertThat(buttons.get(1).getAction(), notNullValue());
//        assertThat(buttons.get(1).getLabel(), is("Закрыть"));
        InvokeAction submit = (InvokeAction) ((MultiAction) drawerPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(0);
        assertThat(submit.getPayload().getDataProvider().getUrl(), is("n2o/data/p/:id/updateWithPrefilters/multi1"));
        RefreshAction refresh = (RefreshAction) ((MultiAction) drawerPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(1);
        assertThat(((RefreshPayload) refresh.getPayload()).getDatasource(), is("p_main"));
        CloseAction close = (CloseAction) ((MultiAction) drawerPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(2);
        assertThat(close.getMeta().getModalsToClose(), is(1));
        assertThat(((CloseActionPayload) close.getPayload()).getPrompt(), is(false));
        ActionContext submitContext = (ActionContext) route("/p/:id/updateWithPrefilters/multi1", CompiledObject.class);
        assertThat(submitContext.getSourceId(null), is("testShowModal"));
        assertThat(submitContext.getOperationId(), is("update"));
        assertThat(submitContext.getOperationId(), is("update"));

        DataSet data = new DataSet();
        data.put("id", 222);
        drawerPage = (SimplePage) read().compile().bind().get(drawerContext, data);
        assertThat(((StandardDatasource) drawerPage.getDatasources().get(drawerPage.getWidget().getId())).getProvider().getUrl(), is("n2o/data/p/222/updateWithPrefilters/modal"));
        submit = (InvokeAction) ((MultiAction) drawerPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(0);
        assertThat(submit.getPayload().getDataProvider().getPathMapping(), not(hasKey("id")));
    }

    @Test
    void updateModelEditWithPreFilters() {
        PageContext pageContext = new PageContext("testOpenDrawerRootPage", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testOpenDrawerRootPage.page.xml")
                .get(pageContext);
        OpenDrawer openDrawer = (OpenDrawer) ((Widget) rootPage.getRegions().get("single").get(0).getContent().get(0))
                .getToolbar().getButton("updateEditWithPrefilters").getAction();
        assertThat(openDrawer.getPayload().getQueryMapping().get("id").getBindLink(), is("models.edit['p_main']"));

        Page openDrawerPage = routeAndGet("/p/updateEditWithPrefilters", Page.class);
        assertThat(openDrawerPage.getId(), is("p_updateEditWithPrefilters"));
    }
}
