package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.action.show_modal.ShowModalPayload;
import net.n2oapp.framework.api.metadata.meta.saga.AsyncMetaSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Button;
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

public class ShowModalCompileTest extends SourceCompileTestBase {

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
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPageSecondFlow.page.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage2.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageDynamicPage.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction1.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction2.page.xml"));
    }

    @Test
    public void create() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);

        Table table = (Table) rootPage.getWidgets().get("p_main");
        ShowModalPayload payload = ((ShowModal) table.getActions().get("create")).getOptions().getPayload();
        //create
        assertThat(payload.getPageUrl(), is("/p/create"));
        assertThat(payload.getSize(), is("sm"));
        assertThat(payload.getPageId(), is("p_create"));

//        assertThat(payload.getActions().size(), is(2));
//        assertThat(payload.getActions().containsKey("submit"), is(true));
//        assertThat(payload.getActions().containsKey("close"), is(true));

//        List<Button> buttons = payload.getToolbar().get(0).getButtons();
//        assertThat(buttons.get(0).getId(), is("submit"));
//        assertThat(buttons.get(0).getActionId(), is("submit"));
//        assertThat(buttons.get(1).getId(), is("close"));
//        assertThat(buttons.get(1).getActionId(), is("close"));

//        InvokeAction submit = (InvokeAction) payload.getActions().get("submit");
//        InvokeActionPayload submitPayload = submit.getOptions().getPayload();
//        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/p/main/create/submit"));
//        assertThat(submitPayload.getModelLink(), is("models.resolve['p_main_create_main']"));

        PageContext modalContext = (PageContext) route("/p/create", Page.class);
        assertThat(modalContext.getSourceId(null), is("testShowModalPage"));
        assertThat(modalContext.getUpload(), is(UploadType.defaults));
        Page modalPage = read().compile().get(modalContext);
        assertThat(modalPage.getId(), is("p_create"));
        assertThat(modalPage.getBreadcrumb(), nullValue());

        assertThat(modalPage.getActions().size(), is(2));
        assertThat(modalPage.getActions().containsKey("submit"), is(true));
        assertThat(modalPage.getActions().containsKey("close"), is(true));

        Widget modalWidget = modalPage.getWidgets().get("p_create_main");
        assertThat(modalWidget.getUpload(), is(UploadType.defaults));

        List<Button> buttons = modalPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.size(), is(2));
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getActionId(), is("submit"));
//        assertThat(buttons.get(0).getLabel(), is("Сохранить"));
        assertThat(buttons.get(1).getId(), is("close"));
        assertThat(buttons.get(1).getActionId(), is("close"));
//        assertThat(buttons.get(1).getLabel(), is("Закрыть"));

        InvokeAction submit = (InvokeAction) modalPage.getActions().get("submit");
        InvokeActionPayload submitPayload = submit.getOptions().getPayload();
        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/p/create/submit"));
        assertThat(submitPayload.getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(submitPayload.getModelLink(), is("models.edit['p_create_main']"));
        assertThat(submitPayload.getWidgetId(), is("p_create_main"));
        AsyncMetaSaga meta = submit.getOptions().getMeta();
        assertThat(meta.getSuccess().getRefresh().getOptions().getWidgetId(), is("p_second"));
        assertThat(meta.getSuccess().getCloseLastModal(), is(true));
        assertThat(meta.getFail().getMessageWidgetId(), is("p_create_main"));
        assertThat(meta.getSuccess().getMessageWidgetId(), is("p_main"));
//        assertThat(meta.getRedirect().getPath(), is("/p/main/:id"));
//        assertThat(meta.getRedirect().getTarget(), is(Target.application));
        assertThat(submit.getOptions().getPayload().getDataProvider().getUrl(), is("n2o/data/p/create/submit"));

        ActionContext submitContext = (ActionContext) route("/p/create/submit", CompiledObject.class);
        assertThat(submitContext.getSourceId(null), is("testShowModal"));
        assertThat(submitContext.getOperationId(), is("create"));
//        assertThat(submitContext.getRedirectUrl(), is("/p/main/:id"));
    }

    @Test
    public void update() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);

        Table table = (Table) rootPage.getWidgets().get("p_main");
        ShowModalPayload payload = ((ShowModal) table.getActions().get("update")).getOptions().getPayload();

        //update
        assertThat(payload.getPageUrl(), is("/p/:p_main_id/update"));
//        assertThat(payload.getTitle(), is("Модальное окно"));
        assertThat(payload.getSize(), is("lg"));

        PageContext modalContext = (PageContext) route("/p/123/update", Page.class);
        assertThat(modalContext.getSourceId(null), is("testShowModalPageSecondFlow"));
        assertThat(modalContext.getPreFilters().size(), is(1));
        assertThat(modalContext.getPreFilters().get(0).getRefWidgetId(), is("main"));
        assertThat(modalContext.getPreFilters().get(0).getRefPageId(), is("p"));
        assertThat(modalContext.getPreFilters().get(0).getFieldId(), is(N2oQuery.Field.PK));
        assertThat(modalContext.getPreFilters().get(0).getType(), is(FilterType.eq));
        assertThat(modalContext.getPreFilters().get(0).getRefModel(), is(ReduxModel.RESOLVE));
        assertThat(modalContext.getPreFilters().get(0).getValue(), is("{secondId}"));
        assertThat(modalContext.getUpload(), is(UploadType.query));
        Page modalPage = read().compile().get(modalContext);
        assertThat(modalPage.getId(), is("p_update"));
        assertThat(modalPage.getBreadcrumb(), nullValue());
        Widget modalWidget = modalPage.getWidgets().get("p_update_main");
        List<Filter> filters = modalWidget.getFilters();
        assertThat(filters.get(0).getParam(), is("id"));
        assertThat(filters.get(0).getFilterId(), is("id"));
        assertThat(filters.get(0).getReloadable(), is(false));
        assertThat(filters.get(0).getLink().getBindLink(), is("models.resolve['p_main']"));
        assertThat(filters.get(0).getLink().getValue(), is("`secondId`"));
        assertThat(modalWidget.getDataProvider().getQueryMapping().get("id").getBindLink(), is("models.resolve['p_main']"));
        assertThat(modalWidget.getDataProvider().getQueryMapping().get("id").getValue(), is("`secondId`"));
        assertThat(modalWidget.getUpload(), is(UploadType.query));
        List<Button> buttons = modalPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.size(), is(2));
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getActionId(), is("submit"));
//        assertThat(buttons.get(0).getLabel(), is("Сохранить"));
        assertThat(buttons.get(1).getId(), is("close"));
        assertThat(buttons.get(1).getActionId(), is("close"));
//        assertThat(buttons.get(1).getLabel(), is("Закрыть"));
        InvokeAction submit = (InvokeAction) modalPage.getActions().get("submit");
        assertThat(submit.getOptions().getMeta().getSuccess().getRefresh().getOptions().getWidgetId(), is("p_main"));
        assertThat(submit.getOptions().getMeta().getSuccess().getCloseLastModal(), is(true));
        assertThat(submit.getOptions().getPayload().getDataProvider().getUrl(), is("n2o/data/p/:p_main_id/update/submit"));
        ActionContext submitContext = (ActionContext) route("/p/:p_main_id/update/submit", CompiledObject.class);
        assertThat(submitContext.getSourceId(null), is("testShowModal"));
        assertThat(submitContext.getOperationId(), is("update"));
        assertThat(submitContext.getOperationId(), is("update"));

        DataSet data = new DataSet();
        data.put("p_main_id", 222);
        modalPage = read().compile().bind().get(modalContext, data);
        ShowModal showModal = (ShowModal) modalPage.getWidgets().get("p_update_main").getActions().get("menuItem0");
        assertThat(showModal.getOptions().getPayload().getPageUrl(), is("/p/222/update/:p_update_main_id/menuItem0"));
        assertThat(modalPage.getWidgets().get("p_update_main").getDataProvider().getUrl(), is("n2o/data/p/222/update"));
        submit = (InvokeAction) modalPage.getActions().get("submit");
        assertThat(submit.getOptions().getPayload().getDataProvider().getPathMapping(), not(hasKey("p_main_id")));// :p_main_id заменяется на этапе биндинга

        QueryContext queryContext = (QueryContext) route("/p/123/update", CompiledQuery.class);
        assertThat(queryContext.getValidations().size(), is(1));
    }

    @Test
    public void createFocus() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);
        Page showModal = routeAndGet("/p/createFocus", Page.class);
        InvokeAction submit = (InvokeAction) showModal.getActions().get("submit");
        assertThat(submit.getOptions().getMeta().getSuccess().getCloseLastModal(), is(true));
        assertThat(submit.getOptions().getMeta().getSuccess().getRedirect().getPath(), is("/p/:id"));
        assertThat(submit.getOptions().getMeta().getSuccess().getRefresh().getOptions().getWidgetId(), is("p_main"));

        CloseAction close = (CloseAction) showModal.getActions().get("close");
        assertThat(close.getOptions().getMeta().getRedirect(), nullValue());
        assertThat(close.getOptions().getMeta().getRefresh(), nullValue());

    }

    @Test
    public void updateFocus() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);
        Page showModal = routeAndGet("/p/123/updateFocus", Page.class);
        InvokeAction submit = (InvokeAction) showModal.getActions().get("submit");
        assertThat(submit.getOptions().getMeta().getSuccess().getCloseLastModal(), is(true));
        assertThat(submit.getOptions().getMeta().getSuccess().getRedirect().getPath(), is("/p/:id"));
        assertThat(submit.getOptions().getMeta().getSuccess().getRefresh().getOptions().getWidgetId(), is("p_main"));

        CloseAction close = (CloseAction) showModal.getActions().get("close");
        assertThat(close.getOptions().getMeta().getRedirect(), nullValue());
        assertThat(close.getOptions().getMeta().getRefresh(), nullValue());
        Widget modalWidget = showModal.getWidgets().get("p_updateFocus_main");
        assertThat(modalWidget.getDataProvider().getPathMapping().size(), is(0));
        assertThat(modalWidget.getDataProvider().getQueryMapping().size(), is(0));
    }

    @Test
    public void createUpdate() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);
        Page showModal = routeAndGet("/p/createUpdate", Page.class);
        InvokeAction submit = (InvokeAction) showModal.getActions().get("submit");
        assertThat(submit.getOptions().getMeta().getSuccess().getCloseLastModal(), is(true));
        assertThat(submit.getOptions().getMeta().getSuccess().getRedirect().getPath(), is("/p/:id/update"));
        //Есть обновление, потому что по умолчанию true. Обновится родительский виджет, потому что close-after-submit=true
        assertThat(submit.getOptions().getMeta().getSuccess().getRefresh().getOptions().getWidgetId(), is("p_main"));
        //Есть уведомление, потому что по умолчанию true. Уведомление будет на родительском виджете, потому что close-after-submit=true

        CloseAction close = (CloseAction) showModal.getActions().get("close");
        assertThat(close.getOptions().getMeta().getRedirect(), nullValue());
        assertThat(close.getOptions().getMeta().getRefresh(), nullValue());

    }

    @Test
    public void dynamicPage() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalDynamicPage.page.xml")
                .get(new PageContext("testShowModalDynamicPage", "/page"));
        PageContext context = (PageContext) route("/page/widget/testOpenPageSimplePageAction1/id1", Page.class);
        DataSet data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction1");
        Page showModal = read().compile().bind().get(context, data);
        assertThat(showModal.getId(), is("page_widget_id1"));
        assertThat(showModal.getLayout().getRegions().get("single").get(0).getItems().get(0).getWidgetId(), is("page_widget_id1_main"));
        assertThat(showModal.getWidgets().size(), is(1));
        assertThat(showModal.getWidgets().get("page_widget_id1_main"), instanceOf(Form.class));

        context = (PageContext) route("/page/widget/testOpenPageSimplePageAction2/id1", Page.class);
        data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction2");
        showModal = read().compile().bind().get(context, data);
        assertThat(showModal.getId(), is("page_widget_id1"));
        assertThat(showModal.getLayout().getRegions().get("single").get(0).getItems().get(0).getWidgetId(), is("page_widget_id1_main"));
        assertThat(showModal.getWidgets().size(), is(1));
        assertThat(showModal.getWidgets().get("page_widget_id1_main"), instanceOf(Form.class));
    }

    @Test
    public void updateWithPreFilters() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);

        PageContext modalContext = (PageContext) route("/p/123/updateWithPrefilters", Page.class);
        assertThat(modalContext.getSourceId(null), is("testShowModalPage"));
        assertThat(modalContext.getPreFilters().size(), is(3));
        assertThat(modalContext.getPreFilters().get(0).getRefWidgetId(), is("main"));
        assertThat(modalContext.getPreFilters().get(0).getRefPageId(), is("p"));
        assertThat(modalContext.getPreFilters().get(0).getFieldId(), is(N2oQuery.Field.PK));
        assertThat(modalContext.getPreFilters().get(0).getType(), is(FilterType.eq));
        assertThat(modalContext.getPreFilters().get(0).getRefModel(), is(ReduxModel.RESOLVE));
        assertThat(modalContext.getPreFilters().get(0).getValue(), is("{id}"));
        //assertThat(modalContext.getPreFilters().get(1).getRefWidgetId(), is("main"));
        //assertThat(modalContext.getPreFilters().get(1).getRefPageId(), is("p"));
        assertThat(modalContext.getPreFilters().get(1).getFieldId(), is("secondId"));
        assertThat(modalContext.getPreFilters().get(1).getType(), is(FilterType.eq));
        //assertThat(modalContext.getPreFilters().get(1).getRefModel(), is(ReduxModel.RESOLVE));
        assertThat(modalContext.getPreFilters().get(1).getValue(), is("1"));
        assertThat(modalContext.getPreFilters().get(2).getRefWidgetId(), is("second"));
        assertThat(modalContext.getPreFilters().get(2).getRefPageId(), is("p"));
        assertThat(modalContext.getPreFilters().get(2).getFieldId(), is("name"));
        assertThat(modalContext.getPreFilters().get(2).getType(), is(FilterType.eq));
        assertThat(modalContext.getPreFilters().get(2).getRefModel(), is(ReduxModel.FILTER));
        assertThat(modalContext.getPreFilters().get(2).getValue(), is("{name}"));
        assertThat(modalContext.getUpload(), is(UploadType.query));

        Page modalPage = read().compile().get(modalContext);
        assertThat(modalPage.getId(), is("p_updateWithPrefilters"));
        assertThat(modalPage.getBreadcrumb(), nullValue());
        Widget modalWidget = modalPage.getWidgets().get("p_updateWithPrefilters_main");
        List<Filter> filters = modalWidget.getFilters();
        assertThat(filters.get(0).getParam(), is("p_main_id"));
        assertThat(filters.get(0).getFilterId(), is("id"));
        assertThat(filters.get(0).getReloadable(), is(false));
        assertThat(filters.get(0).getLink().getBindLink(), is("models.resolve['p_main']"));
        assertThat(filters.get(0).getLink().getValue(), is("`id`"));
        assertThat(filters.get(1).getParam(), is("secondId"));
        assertThat(filters.get(1).getFilterId(), is("secondId"));
        assertThat(filters.get(1).getReloadable(), is(false));
        assertThat(filters.get(1).getLink().getBindLink(), nullValue());
        assertThat(filters.get(1).getLink().getValue(), is(1));
        assertThat(filters.get(2).getParam(), is("name"));
        assertThat(filters.get(2).getFilterId(), is("name_eq"));
        assertThat(filters.get(2).getReloadable(), is(false));
        assertThat(filters.get(2).getLink().getBindLink(), is("models.filter['p_second']"));
        assertThat(filters.get(2).getLink().getValue(), is("`name`"));

        assertThat(modalWidget.getDataProvider().getPathMapping().get("p_main_id").getBindLink(), is("models.resolve['p_main'].id"));
        assertThat(modalWidget.getDataProvider().getQueryMapping().get("secondId").getBindLink(), nullValue());
        assertThat(modalWidget.getDataProvider().getQueryMapping().get("secondId").getValue(), is(1));
        assertThat(modalWidget.getDataProvider().getQueryMapping().get("name").getBindLink(), is("models.filter['p_second']"));
        assertThat(modalWidget.getDataProvider().getQueryMapping().get("name").getValue(), is("`name`"));

        assertThat(modalWidget.getUpload(), is(UploadType.query));
        List<Button> buttons = modalPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.size(), is(2));
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getActionId(), is("submit"));
//        assertThat(buttons.get(0).getLabel(), is("Сохранить"));
        assertThat(buttons.get(1).getId(), is("close"));
        assertThat(buttons.get(1).getActionId(), is("close"));
//        assertThat(buttons.get(1).getLabel(), is("Закрыть"));
        InvokeAction submit = (InvokeAction) modalPage.getActions().get("submit");
        assertThat(submit.getOptions().getMeta().getSuccess().getRefresh().getOptions().getWidgetId(), is("p_main"));
        assertThat(submit.getOptions().getMeta().getSuccess().getCloseLastModal(), is(true));
        assertThat(submit.getOptions().getPayload().getDataProvider().getUrl(), is("n2o/data/p/:p_main_id/updateWithPrefilters/submit"));
        ActionContext submitContext = (ActionContext) route("/p/:p_main_id/updateWithPrefilters/submit", CompiledObject.class);
        assertThat(submitContext.getSourceId(null), is("testShowModal"));
        assertThat(submitContext.getOperationId(), is("update"));
        assertThat(submitContext.getOperationId(), is("update"));

        DataSet data = new DataSet();
        data.put("p_main_id", 222);
        modalPage = read().compile().bind().get(modalContext, data);
        assertThat(modalPage.getWidgets().get("p_updateWithPrefilters_main").getDataProvider().getUrl(), is("n2o/data/p/222/updateWithPrefilters"));
        submit = (InvokeAction) modalPage.getActions().get("submit");
        assertThat(submit.getOptions().getPayload().getDataProvider().getPathMapping(), not(hasKey("p_main_id")));
    }

    @Test
    public void updateModelEditWithPreFilters() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);
        ShowModal showModal = (ShowModal) rootPage.getWidgets().get("p_main").getActions().get("updateEditWithPrefilters");
        assertThat(showModal.getOptions().getPayload().getQueryMapping().get("id").getBindLink(), is("models.edit['p_main']"));
    }

}